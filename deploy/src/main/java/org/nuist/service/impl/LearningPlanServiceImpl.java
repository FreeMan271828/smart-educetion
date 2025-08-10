package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nuist.bo.LearningPlanBO;
import org.nuist.mapper.LearningPlanMapper;
import org.nuist.po.LearningPlanPO;
import org.nuist.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 个性化学习计划服务实现类
 */
@Service
public class LearningPlanServiceImpl extends ServiceImpl<LearningPlanMapper, LearningPlanPO> implements LearningPlanService {
    private final WebClient webClient;
    private final LearningPlanMapper learningPlanMapper;
    private final CourseService courseService;
    private final KnowledgeService knowledgeService;
    private final KnowledgeUnitService knowledgeUnitService;
    private final SubjectService subjectService;
    
    @Autowired
    private LearningProgressService learningProgressService;

    public LearningPlanServiceImpl(WebClient webClient, LearningPlanMapper learningPlanMapper, CourseService courseService, KnowledgeService knowledgeService, KnowledgeUnitService knowledgeUnitService, SubjectService subjectService) {
        this.webClient = webClient;
        this.learningPlanMapper = learningPlanMapper;
        this.courseService = courseService;
        this.knowledgeService = knowledgeService;
        this.knowledgeUnitService = knowledgeUnitService;
        this.subjectService = subjectService;
    }






    @Override
    @Transactional
    public Map<String, Object> generateLearningPlan(Long studentId, String targetGoal, Integer timeFrame,
                                                    Long courseId) {


        // 1. 参数校验
        validateParams(studentId, targetGoal, timeFrame, courseId);

        // 2. 获取课程名称（用于后续提示词）

            String courseName = courseService.getCourseById(courseId).getName();

            String subjectName = new String();
            //处理课程没有对应科目的情况
        if(subjectService.getSubjectByCourseId(courseId)!=null ) {
            subjectName = subjectService.getSubjectByCourseId(courseId).getName();
        }
        else{
            subjectName = courseName;
        }

        // 3. 调用知识点分析服务 [关键变更点]
        Map<String, Object> knowledgeResult = knowledgeUnitService.analyzeTargetKnowledge(targetGoal, subjectName);
        List<Map<String, Object>> knowledgeUnits = (List<Map<String, Object>>) knowledgeResult.get("knowledge_units");
        List<String> knowledgeNames = knowledgeUnits.stream()
                .map(unit -> (String) unit.get("name"))
                .collect(Collectors.toList());

        // 4. 构建AI提示词（基于知识点） [关键变更点]
        String prompt = buildKnowledgeBasedPrompt(targetGoal, timeFrame, knowledgeNames);

        // 5. 调用RAG服务
        Map<String, Object> aiResponse = callRAGService(prompt);

        // 6. 保存到数据库
        LearningPlanPO savedPlan = saveLearningPlanToDB(studentId, targetGoal, timeFrame, aiResponse);

        // 7. 构造返回结果（包含知识点信息）
        return buildResponseMap(savedPlan, aiResponse, knowledgeUnits);
    }

    //--- 参数校验（新增courseId校验） ---//
    private void validateParams(Long studentId, String targetGoal, Integer timeFrame, Long courseId) {
        if (studentId == null || !StringUtils.hasText(targetGoal)
                || timeFrame == null || timeFrame <= 0 || courseId == null) {
            throw new IllegalArgumentException("参数错误：studentId、targetGoal、timeFrame和courseId必须有效");
        }
    }

    //--- 重构提示词构建方法 ---//
    private String buildKnowledgeBasedPrompt(String targetGoal, int timeFrame, List<String> knowledgeNames) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一名资深教育规划师，需基于指定知识点创建可执行的个性化学习计划。\n")
                .append("**必须严格遵守以下要求：**\n")
                .append("1. 请分析指定知识点中哪些与计划相关，目标包含的知识点应该被尽量安排进计划中，并按难度顺序由易到难安排学习计划\n")
                .append("2. 每天安排1-4个知识点（根据复杂度调整）与目标关联度高的知识点优先安排学习，关联度低的安排在后部或者不安排\n")
                .append("3. 输出为纯JSON格式，结构如下：\n")
                .append("{\n")
                .append("  \"planName\": \"计划名称\",\n")
                .append("  \"dailyActivities\": [\n")
                .append("    {\"day\": 1, \"knowledgePoints\": [\"知识点A\", \"知识点B\"], \"resources\": [\"资源1\", \"资源2\"]},\n")
                .append("    ...\n")
                .append("  ],\n")
                .append("  \"recommendedResources\": {\n")
                .append("    \"video\": [\"视频资源1\"],\n")
                .append("    \"article\": [\"文章资源1\"]\n")
                .append("  }\n")
                .append("}\n\n")
                .append("**任务参数：**\n")
                .append("- 学习目标: ").append(targetGoal).append("\n")
                .append("- 时间周期: ").append(timeFrame).append("天\n")
                .append("- 需要覆盖目标有关知识点，无关知识点无需理会\n")
                .append(String.join(", ", knowledgeNames)).append("\n\n")
                .append("**每日计划示例：**\n")
                .append("第1天: 基础概念学习（知识点A, 知识点B）\n")
                .append("第2天: 进阶应用（知识点C）\n");

        return prompt.toString();
    }

    //--- 重构返回结果构建方法（加入知识点信息） ---//
    private Map<String, Object> buildResponseMap(LearningPlanPO savedPlan,
                                                 Map<String, Object> aiResponse,
                                                 List<Map<String, Object>> knowledgeUnits) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("planId", savedPlan.getPlanId());
        response.put("studentId", savedPlan.getStudentId());
        response.put("planName", savedPlan.getName());
        response.put("beginAt", savedPlan.getBeginAt());
        response.put("endAt", savedPlan.getEndAt());
        response.put("dailyActivities", aiResponse.get("dailyActivities"));
        response.put("recommendedResources", aiResponse.get("recommendedResources"));
        response.put("knowledgeUnits", knowledgeUnits); // 新增知识点信息
        return response;
    }

    //--- RAG服务调用（关键修改）---//
    private Map<String, Object> callRAGService(String prompt) {
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        requestBody.put("messages", Collections.singletonList(message));

        // 调用RAG服务
        Map<String, Object> response = webClient.post()
                .uri("/chat/plain")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block(Duration.ofSeconds(90));

        // 解析AI响应（带预处理和安全解析）
        String aiResponse = (String) response.get("answer");
        try {
            return parseAndCleanJsonResponse(aiResponse);
        } catch (Exception e) {
            throw new RuntimeException("AI响应解析失败: " + aiResponse, e);
        }
    }

    //--- 新增：JSON响应处理 ---//
    private Map<String, Object> parseAndCleanJsonResponse(String rawResponse) throws JsonProcessingException {
        // 空响应处理
        if (rawResponse == null || rawResponse.isBlank()) {
            return Collections.emptyMap();
        }

        // 清洗响应
        String cleanJson = rawResponse.trim()
                .replaceFirst("^```(json)?", "")  // 去除开头的```json或```
                .replaceFirst("```$", "")         // 去除结尾的```
                .trim();

        // 安全解析
        return parseJsonSafely(cleanJson);
    }

    //--- 新增：安全解析JSON ---//
    private Map<String, Object> parseJsonSafely(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        // 使用树模型解析
        JsonNode rootNode = mapper.readTree(json);
        return mapper.convertValue(rootNode, new TypeReference<Map<String, Object>>() {});
    }

    @Transactional
    protected LearningPlanPO saveLearningPlanToDB(Long studentId, String targetGoal, int timeFrame,
                                                  Map<String, Object> aiResponse) {
        LearningPlanPO plan = new LearningPlanPO();
        plan.setStudentId(studentId);
        plan.setName((String) aiResponse.get("planName"));
        plan.setBeginAt(LocalDateTime.now());
        plan.setEndAt(LocalDateTime.now().plusDays(timeFrame));
        plan.setCompleted(false);
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());

        // 序列化完整的AI响应
        try {
            plan.setContent(new ObjectMapper().writeValueAsString(aiResponse));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON序列化失败", e);
        }

        learningPlanMapper.insert(plan);
        return plan;
    }






    /**
     * 获取当前学习计划（已开始未结束）
     */
    public List<LearningPlanBO> getCurrentLearningPlan(Long studentId) {
        LocalDateTime now = LocalDateTime.now();
        QueryWrapper<LearningPlanPO> query = new QueryWrapper<>();
        query.eq("student_id", studentId)
                .le("begin_at", now)  // 开始时间 <= 当前时间
                .ge("end_at", now);   // 结束时间 >= 当前时间

        return this.list(query).stream()
                .map(LearningPlanBO::fromLearningPlanPO)
                .collect(Collectors.toList());
    }

    /**
     * 获取历史学习计划（已结束）
     */
    public List<LearningPlanBO> getLearningPlanHistory(Long studentId) {
        LocalDateTime now = LocalDateTime.now();
        QueryWrapper<LearningPlanPO> query = new QueryWrapper<>();
        query.eq("student_id", studentId)
                .lt("end_at", now);    // 结束时间 < 当前时间

        return this.list(query).stream()
                .map(LearningPlanBO::fromLearningPlanPO)
                .collect(Collectors.toList());
    }

    /**
     * 按名称模糊搜索学习计划
     * 返回 List<LearningPlanBO> 并使用 from 方法转换
     */
    public List<LearningPlanBO> searchLearningPlans(Long studentId, String keywords) {
        QueryWrapper<LearningPlanPO> query = new QueryWrapper<>();
        query.eq("student_id", studentId)
                .like("name", keywords);  // 名称模糊匹配

        return this.list(query).stream()
                .map(LearningPlanBO::fromLearningPlanPO) // 使用 BO 类的 from 方法转换
                .collect(Collectors.toList());
    }

    /**
     * 更新学习计划
     */
    public LearningPlanBO updatePlan(LearningPlanBO learningPlanBO) {
        // 1. 获取现有计划
        LearningPlanPO existingPO = getById(learningPlanBO.getPlanId());
        if (existingPO == null) {
            throw new RuntimeException("学习计划不存在");
        }

        // 2. 部分字段更新（只处理非空字段）
        LambdaUpdateWrapper<LearningPlanPO> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(LearningPlanPO::getUpdatedAt, LocalDateTime.now()); // 总是更新修改时间

        // 检查并设置需要更新的字段
        if (learningPlanBO.getPlanName() != null) {
            updateWrapper.set(LearningPlanPO::getName, learningPlanBO.getPlanName());
        }
        if (learningPlanBO.getContent() != null) {
            updateWrapper.set(LearningPlanPO::getContent, learningPlanBO.getContent());
        }
        if (learningPlanBO.getBeginAt() != null) {
            updateWrapper.set(LearningPlanPO::getBeginAt, learningPlanBO.getBeginAt());
        }
        if (learningPlanBO.getEndAt() != null) {
            updateWrapper.set(LearningPlanPO::getEndAt, learningPlanBO.getEndAt());
        }
        if (learningPlanBO.isCompletedSet()) {  // 新增检查方法
            updateWrapper.set(LearningPlanPO::isCompleted, learningPlanBO.isCompleted());
        } else {
            // 未传值时保持原值
            updateWrapper.set(LearningPlanPO::isCompleted, existingPO.isCompleted());
        }

        // 3. 设置更新条件
        updateWrapper.eq(LearningPlanPO::getPlanId, learningPlanBO.getPlanId());

        // 4. 执行更新
        boolean success = update(updateWrapper);

        if (success) {
            return LearningPlanBO.fromLearningPlanPO(getById(learningPlanBO.getPlanId()));
        } else {
            throw new RuntimeException("更新学习计划失败");
        }
    }

    /**
     * 获取特定日期的学习计划内容
     * 要求：date必须在begin_at和end_at之间（含当天）
     */
    public List<LearningPlanBO> getDailyPlanActivities(Long studentId, LocalDate date) {
        // 将LocalDate转换为当天的开始时间（00:00）和结束时间（23:59）
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        QueryWrapper<LearningPlanPO> query = new QueryWrapper<>();
        query.eq("student_id", studentId)
                .le("begin_at", endOfDay)   // 开始时间 <= 当天结束时间
                .ge("end_at", startOfDay);  // 结束时间 >= 当天开始时间

        return list(query).stream()
                .map(LearningPlanBO::fromLearningPlanPO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Map<String, Object>> getDailyPlanByName(Long studentId, String planName, LocalDate date) {
        if (studentId == null || !StringUtils.hasText(planName) || date == null) {
            return new ArrayList<>();
        }
        
        // 实际实现中应该从数据库查询指定计划名称和日期的学习计划活动
        // 这里返回模拟数据
        
        // 假设计划名称为"Java编程"
        if (planName.toLowerCase().contains("java")) {
            return generateSampleActivities();
        }
        
        return new ArrayList<>();
    }
    
    @Override
    public List<Map<String, Object>> getPlanRecommendedResources(String planId) {
        if (!StringUtils.hasText(planId)) {
            return new ArrayList<>();
        }
        
        // 实际实现中应该从数据库查询指定计划ID的推荐资源
        // 这里返回模拟数据
        return generateSampleResources();
    }
    
    @Override
    public List<Map<String, Object>> getPlanResourcesByName(Long studentId, String planName) {
        if (studentId == null || !StringUtils.hasText(planName)) {
            return new ArrayList<>();
        }
        
        // 实际实现中应该从数据库查询指定计划名称的推荐资源
        // 这里返回模拟数据
        return generateSampleResources();
    }
    
    @Override
    public List<Map<String, Object>> searchPlanResources(Long studentId, String keywords) {
        if (studentId == null || !StringUtils.hasText(keywords)) {
            return new ArrayList<>();
        }
        
        // 实际实现中应该从数据库搜索匹配关键词的资源
        // 这里返回模拟数据
        
        // 假设关键词为"Java"
        if (keywords.toLowerCase().contains("java")) {
            List<Map<String, Object>> resources = new ArrayList<>();
            
            Map<String, Object> resource1 = new HashMap<>();
            resource1.put("resourceId", "res-java-1");
            resource1.put("title", "Java编程思想");
            resource1.put("type", "book");
            resource1.put("url", "https://example.com/java-book");
            resource1.put("description", "Java编程入门经典书籍");
            resources.add(resource1);
            
            Map<String, Object> resource2 = new HashMap<>();
            resource2.put("resourceId", "res-java-2");
            resource2.put("title", "Java核心技术视频教程");
            resource2.put("type", "video");
            resource2.put("url", "https://example.com/java-video");
            resource2.put("description", "深入讲解Java核心技术的视频课程");
            resources.add(resource2);
            
            return resources;
        }
        
        return new ArrayList<>();
    }

    @Override
    public List<LearningPlanBO> getifCompletedLearningPlans(Long studentId, boolean b) {
        QueryWrapper<LearningPlanPO> query = new QueryWrapper<>();
        query.eq("student_id", studentId)
                .eq("completed", b);
        return this.list(query).stream()
                .map(LearningPlanBO::fromLearningPlanPO) // 使用 BO 类的 from 方法转换
                .collect(Collectors.toList());

    }

    @Override
    public boolean deletePlan(Long planId) {
        int result= baseMapper.deleteById(planId);
        return result>0;
    }

    @Override
    public LearningPlanBO addPlan(LearningPlanBO learningPlanBO) {

        LearningPlanPO learningPlanPO = LearningPlanBO.toLearningPlanPO(learningPlanBO);
        boolean success =baseMapper.insert(learningPlanPO)>0;
        if (success) {
            return LearningPlanBO.fromLearningPlanPO(learningPlanPO);
        } else {
            throw new RuntimeException("添加学习计划失败");
        }
    }


    /**
     * 生成每日活动
     * @param studentId 学生ID
     * @param timeFrame 时间框架
     * @param courseIds 课程ID列表
     * @param knowledgeIds 知识点ID列表
     * @return 每日活动列表
     */
    private List<Map<String, Object>> generateDailyActivities(Long studentId, Integer timeFrame, 
                                                            List<Long> courseIds, List<Long> knowledgeIds) {
        List<Map<String, Object>> dailyActivities = new ArrayList<>();
        
        // 获取学生学习进度，为生成计划提供依据
        // 实际实现中应该根据学生的学习进度和课程、知识点信息生成个性化学习计划
        // 这里简化处理，平均分配学习任务
        
        for (int day = 1; day <= timeFrame; day++) {
            Map<String, Object> dailyActivity = new HashMap<>();
            dailyActivity.put("day", day);
            dailyActivity.put("date", LocalDate.now().plusDays(day - 1));
            
            List<Map<String, Object>> activities = new ArrayList<>();
            
            // 生成2-4个学习活动
            int activityCount = 2 + (day % 3); // 2-4个活动
            for (int i = 0; i < activityCount; i++) {
                Map<String, Object> activity = new HashMap<>();
                activity.put("activityId", "activity-" + day + "-" + i);
                activity.put("type", getRandomActivityType());
                activity.put("title", "学习活动 " + (i + 1));
                activity.put("description", "第" + day + "天的第" + (i + 1) + "个学习活动");
                activity.put("duration", 30 + (i * 15)); // 30-60分钟
                activity.put("status", "pending");
                activities.add(activity);
            }
            
            dailyActivity.put("activities", activities);
            dailyActivities.add(dailyActivity);
        }
        
        return dailyActivities;
    }
    
    /**
     * 生成推荐资源
     * @param studentId 学生ID
     * @param courseIds 课程ID列表
     * @param knowledgeIds 知识点ID列表
     * @return 推荐资源列表
     */
    private List<Map<String, Object>> generateRecommendedResources(Long studentId, 
                                                                 List<Long> courseIds, 
                                                                 List<Long> knowledgeIds) {
        // 在实际实现中，应该根据学生的学习进度、课程和知识点信息生成个性化推荐资源
        // 这里简化处理，返回一些示例资源
        return generateSampleResources();
    }
    
    /**
     * 将课程名称转换为课程ID
     * @param courseNames 课程名称列表
     * @return 课程ID列表
     */
    private List<Long> convertCourseNamesToIds(List<String> courseNames) {
        // 实际实现中应该查询数据库，将课程名称转换为课程ID
        // 这里简化处理，返回模拟ID
        List<Long> courseIds = new ArrayList<>();
        for (int i = 0; i < courseNames.size(); i++) {
            courseIds.add((long) (i + 1));
        }
        return courseIds;
    }
    
    /**
     * 将知识点名称转换为知识点ID
     * @param knowledgeNames 知识点名称列表
     * @return 知识点ID列表
     */
    private List<Long> convertKnowledgeNamesToIds(List<String> knowledgeNames) {
        // 实际实现中应该查询数据库，将知识点名称转换为知识点ID
        // 这里简化处理，返回模拟ID
        List<Long> knowledgeIds = new ArrayList<>();
        for (int i = 0; i < knowledgeNames.size(); i++) {
            knowledgeIds.add((long) (i + 101));
        }
        return knowledgeIds;
    }
    
    /**
     * 获取随机活动类型
     * @return 活动类型
     */
    private String getRandomActivityType() {
        String[] types = {"reading", "exercise", "quiz", "video", "practice"};
        return types[(int) (Math.random() * types.length)];
    }
    
    /**
     * 生成示例活动
     * @return 示例活动列表
     */
    private List<Map<String, Object>> generateSampleActivities() {
        List<Map<String, Object>> activities = new ArrayList<>();
        
        Map<String, Object> activity1 = new HashMap<>();
        activity1.put("activityId", "activity-sample-1");
        activity1.put("type", "reading");
        activity1.put("title", "Java基础语法");
        activity1.put("description", "学习Java基础语法和数据类型");
        activity1.put("duration", 45);
        activity1.put("status", "pending");
        activities.add(activity1);
        
        Map<String, Object> activity2 = new HashMap<>();
        activity2.put("activityId", "activity-sample-2");
        activity2.put("type", "exercise");
        activity2.put("title", "Java变量和表达式练习");
        activity2.put("description", "完成10道关于Java变量和表达式的练习题");
        activity2.put("duration", 30);
        activity2.put("status", "pending");
        activities.add(activity2);
        
        Map<String, Object> activity3 = new HashMap<>();
        activity3.put("activityId", "activity-sample-3");
        activity3.put("type", "video");
        activity3.put("title", "Java面向对象编程视频课程");
        activity3.put("description", "观看关于Java面向对象编程的视频教程");
        activity3.put("duration", 60);
        activity3.put("status", "pending");
        activities.add(activity3);
        
        return activities;
    }
    
    /**
     * 生成示例资源
     * @return 示例资源列表
     */
    private List<Map<String, Object>> generateSampleResources() {
        List<Map<String, Object>> resources = new ArrayList<>();
        
        Map<String, Object> resource1 = new HashMap<>();
        resource1.put("resourceId", "res-sample-1");
        resource1.put("title", "Java编程思想");
        resource1.put("type", "book");
        resource1.put("url", "https://example.com/java-book");
        resource1.put("description", "Java编程入门经典书籍");
        resources.add(resource1);
        
        Map<String, Object> resource2 = new HashMap<>();
        resource2.put("resourceId", "res-sample-2");
        resource2.put("title", "Java核心技术视频教程");
        resource2.put("type", "video");
        resource2.put("url", "https://example.com/java-video");
        resource2.put("description", "深入讲解Java核心技术的视频课程");
        resources.add(resource2);
        
        Map<String, Object> resource3 = new HashMap<>();
        resource3.put("resourceId", "res-sample-3");
        resource3.put("title", "Java编程练习平台");
        resource3.put("type", "practice");
        resource3.put("url", "https://example.com/java-practice");
        resource3.put("description", "提供丰富的Java编程练习题和实时反馈");
        resources.add(resource3);
        
        return resources;
    }
} 