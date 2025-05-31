package org.nuist.service.impl;

import org.nuist.service.LearningPlanService;
import org.nuist.service.LearningProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 个性化学习计划服务实现类
 */
@Service
public class LearningPlanServiceImpl implements LearningPlanService {
    
    @Autowired
    private LearningProgressService learningProgressService;
    
    @Override
    public Map<String, Object> generateLearningPlan(Long studentId, String targetGoal, Integer timeFrame, 
                                                  List<Long> courseIds, List<Long> knowledgeIds) {
        if (studentId == null || !StringUtils.hasText(targetGoal) || timeFrame == null || timeFrame <= 0) {
            return new HashMap<>();
        }
        
        Map<String, Object> learningPlan = new HashMap<>();
        String planId = generatePlanId();
        
        // 计划基本信息
        learningPlan.put("planId", planId);
        learningPlan.put("studentId", studentId);
        learningPlan.put("targetGoal", targetGoal);
        learningPlan.put("timeFrame", timeFrame);
        learningPlan.put("createdAt", LocalDateTime.now());
        
        // 生成每日活动
        List<Map<String, Object>> dailyActivities = generateDailyActivities(
                studentId, timeFrame, courseIds, knowledgeIds);
        learningPlan.put("dailyActivities", dailyActivities);
        
        // 添加资源推荐
        List<Map<String, Object>> recommendedResources = generateRecommendedResources(
                studentId, courseIds, knowledgeIds);
        learningPlan.put("recommendedResources", recommendedResources);
        
        // 保存计划（实际实现中可能需要保存到数据库）
        
        return learningPlan;
    }
    
    @Override
    public Map<String, Object> generateLearningPlanByCourseName(Long studentId, String targetGoal, 
                                                              Integer timeFrame, List<String> courseNames) {
        if (studentId == null || !StringUtils.hasText(targetGoal) || 
                timeFrame == null || timeFrame <= 0 || courseNames == null || courseNames.isEmpty()) {
            return new HashMap<>();
        }
        
        // 转换课程名称为课程ID
        List<Long> courseIds = convertCourseNamesToIds(courseNames);
        
        return generateLearningPlan(studentId, targetGoal, timeFrame, courseIds, null);
    }
    
    @Override
    public Map<String, Object> generateLearningPlanByKnowledgeName(Long studentId, String targetGoal, 
                                                                 Integer timeFrame, List<String> knowledgeNames) {
        if (studentId == null || !StringUtils.hasText(targetGoal) || 
                timeFrame == null || timeFrame <= 0 || knowledgeNames == null || knowledgeNames.isEmpty()) {
            return new HashMap<>();
        }
        
        // 转换知识点名称为知识点ID
        List<Long> knowledgeIds = convertKnowledgeNamesToIds(knowledgeNames);
        
        return generateLearningPlan(studentId, targetGoal, timeFrame, null, knowledgeIds);
    }
    
    @Override
    public Map<String, Object> getCurrentLearningPlan(Long studentId) {
        if (studentId == null) {
            return new HashMap<>();
        }
        
        // 实际实现中应该从数据库查询当前生效的学习计划
        // 这里返回模拟数据
        Map<String, Object> currentPlan = new HashMap<>();
        currentPlan.put("planId", "plan-123456");
        currentPlan.put("studentId", studentId);
        currentPlan.put("targetGoal", "掌握Java编程");
        currentPlan.put("timeFrame", 30);
        currentPlan.put("createdAt", LocalDateTime.now().minusDays(5));
        currentPlan.put("progress", 25); // 25% 完成率
        
        List<Map<String, Object>> dailyActivities = new ArrayList<>();
        Map<String, Object> activity = new HashMap<>();
        activity.put("day", 1);
        activity.put("activities", generateSampleActivities());
        dailyActivities.add(activity);
        
        currentPlan.put("dailyActivities", dailyActivities);
        
        return currentPlan;
    }
    
    @Override
    public List<Map<String, Object>> getLearningPlanHistory(Long studentId) {
        if (studentId == null) {
            return new ArrayList<>();
        }
        
        // 实际实现中应该从数据库查询历史学习计划
        // 这里返回模拟数据
        List<Map<String, Object>> historyPlans = new ArrayList<>();
        
        Map<String, Object> plan1 = new HashMap<>();
        plan1.put("planId", "plan-history-1");
        plan1.put("studentId", studentId);
        plan1.put("targetGoal", "SQL数据库入门");
        plan1.put("timeFrame", 14);
        plan1.put("createdAt", LocalDateTime.now().minusDays(60));
        plan1.put("completedAt", LocalDateTime.now().minusDays(46));
        plan1.put("completionRate", 85); // 85% 完成率
        historyPlans.add(plan1);
        
        Map<String, Object> plan2 = new HashMap<>();
        plan2.put("planId", "plan-history-2");
        plan2.put("studentId", studentId);
        plan2.put("targetGoal", "Web前端基础");
        plan2.put("timeFrame", 21);
        plan2.put("createdAt", LocalDateTime.now().minusDays(100));
        plan2.put("completedAt", LocalDateTime.now().minusDays(79));
        plan2.put("completionRate", 92); // 92% 完成率
        historyPlans.add(plan2);
        
        return historyPlans;
    }
    
    @Override
    public List<Map<String, Object>> searchLearningPlans(Long studentId, String keywords) {
        if (studentId == null || !StringUtils.hasText(keywords)) {
            return new ArrayList<>();
        }
        
        // 实际实现中应该从数据库搜索匹配关键词的学习计划
        // 这里返回模拟数据
        List<Map<String, Object>> matchedPlans = new ArrayList<>();
        
        // 假设关键词为"Java"
        if (keywords.toLowerCase().contains("java")) {
            Map<String, Object> plan = new HashMap<>();
            plan.put("planId", "plan-java-123");
            plan.put("studentId", studentId);
            plan.put("targetGoal", "Java编程入门到精通");
            plan.put("timeFrame", 45);
            plan.put("createdAt", LocalDateTime.now().minusDays(30));
            plan.put("progress", 40); // 40% 完成率
            matchedPlans.add(plan);
        }
        
        return matchedPlans;
    }
    
    @Override
    public Map<String, Object> updatePlanProgress(String planId, String activityId, String status, String feedback) {
        if (!StringUtils.hasText(planId) || !StringUtils.hasText(activityId) || !StringUtils.hasText(status)) {
            return new HashMap<>();
        }
        
        // 实际实现中应该更新数据库中的学习计划进度
        // 这里返回模拟数据
        Map<String, Object> updatedPlan = new HashMap<>();
        updatedPlan.put("planId", planId);
        updatedPlan.put("activityId", activityId);
        updatedPlan.put("status", status);
        if (StringUtils.hasText(feedback)) {
            updatedPlan.put("feedback", feedback);
        }
        updatedPlan.put("updatedAt", LocalDateTime.now());
        
        // 模拟更新后的完成率
        updatedPlan.put("newCompletionRate", 45); // 假设更新后达到45%
        
        return updatedPlan;
    }
    
    @Override
    public List<Map<String, Object>> getDailyPlanActivities(Long studentId, LocalDate date) {
        if (studentId == null || date == null) {
            return new ArrayList<>();
        }
        
        // 实际实现中应该从数据库查询指定日期的学习计划活动
        // 这里返回模拟数据
        
        // 只返回当天和未来日期的计划
        if (date.isBefore(LocalDate.now())) {
            return new ArrayList<>(); // 过去的日期返回空列表
        }
        
        return generateSampleActivities();
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
    
    /**
     * 生成计划ID
     * @return 计划ID
     */
    private String generatePlanId() {
        return "plan-" + UUID.randomUUID().toString().substring(0, 8);
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