package org.nuist.service.impl;

import org.nuist.service.StudentAssistantService;
import org.nuist.service.LearningProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 学生在线学习助手服务实现类
 */
@Service
public class StudentAssistantServiceImpl implements StudentAssistantService {
    
    @Autowired
    private LearningProgressService learningProgressService;
    
    @Override
    public Map<String, Object> askQuestion(Long studentId, String question, Long courseId) {
        if (studentId == null || !StringUtils.hasText(question)) {
            return new HashMap<>();
        }
        
        // 实际实现中，这里可能需要调用自然语言处理服务或AI服务来生成回答
        // 这里简化实现，返回模拟数据
        Map<String, Object> response = new HashMap<>();
        response.put("questionId", generateQuestionId());
        response.put("studentId", studentId);
        response.put("question", question);
        
        if (courseId != null) {
            response.put("courseId", courseId);
            // 查询课程名称（实际实现中应该从数据库获取）
            response.put("courseName", "课程" + courseId);
        }
        
        response.put("answer", generateSampleAnswer(question));
        response.put("confidence", 0.85);
        response.put("timestamp", LocalDateTime.now());
        
        // 添加相关资源推荐
        response.put("relatedResources", generateSampleResources());
        
        return response;
    }
    
    @Override
    public Map<String, Object> askQuestionByCourseName(Long studentId, String question, String courseName) {
        if (studentId == null || !StringUtils.hasText(question) || !StringUtils.hasText(courseName)) {
            return new HashMap<>();
        }
        
        // 通过课程名称查找课程ID（实际实现中应该查询数据库）
        Long courseId = findCourseIdByName(courseName);
        
        if (courseId == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "未找到课程: " + courseName);
            return errorResponse;
        }
        
        Map<String, Object> response = askQuestion(studentId, question, courseId);
        response.put("courseName", courseName);
        
        return response;
    }
    
    @Override
    public Map<String, Object> askQuestionByKnowledgeName(Long studentId, String question, String knowledgeName) {
        if (studentId == null || !StringUtils.hasText(question) || !StringUtils.hasText(knowledgeName)) {
            return new HashMap<>();
        }
        
        // 通过知识点名称查找知识点ID和所属课程ID（实际实现中应该查询数据库）
        Long knowledgeId = findKnowledgeIdByName(knowledgeName);
        Long courseId = findCourseIdByKnowledgeId(knowledgeId);
        
        if (knowledgeId == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "未找到知识点: " + knowledgeName);
            return errorResponse;
        }
        
        Map<String, Object> response = askQuestion(studentId, question, courseId);
        response.put("knowledgeId", knowledgeId);
        response.put("knowledgeName", knowledgeName);
        
        return response;
    }
    
    @Override
    public Map<String, Object> generateExercise(Long studentId, Long courseId, List<Long> knowledgeIds, 
                                              String difficultyLevel, Integer questionCount) {
        if (studentId == null || courseId == null || questionCount == null || questionCount <= 0) {
            return new HashMap<>();
        }
        
        // 实际实现中，应该根据学生的学习进度、知识点掌握情况和难度要求生成个性化题目
        // 这里简化实现，返回模拟数据
        Map<String, Object> exercise = new HashMap<>();
        String exerciseId = generateExerciseId();
        
        exercise.put("exerciseId", exerciseId);
        exercise.put("studentId", studentId);
        exercise.put("courseId", courseId);
        exercise.put("difficultyLevel", StringUtils.hasText(difficultyLevel) ? difficultyLevel : "medium");
        exercise.put("generatedAt", LocalDateTime.now());
        
        // 知识点信息
        if (knowledgeIds != null && !knowledgeIds.isEmpty()) {
            exercise.put("knowledgeIds", knowledgeIds);
            // 实际实现中应该查询知识点名称
        }
        
        // 生成题目
        List<Map<String, Object>> questions = generateSampleQuestions(questionCount);
        exercise.put("questions", questions);
        exercise.put("questionCount", questions.size());
        
        return exercise;
    }
    
    @Override
    public Map<String, Object> generateExerciseByCourseName(Long studentId, String courseName, 
                                                          String difficultyLevel, Integer questionCount) {
        if (studentId == null || !StringUtils.hasText(courseName) || questionCount == null || questionCount <= 0) {
            return new HashMap<>();
        }
        
        // 通过课程名称查找课程ID
        Long courseId = findCourseIdByName(courseName);
        
        if (courseId == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "未找到课程: " + courseName);
            return errorResponse;
        }
        
        Map<String, Object> exercise = generateExercise(studentId, courseId, null, difficultyLevel, questionCount);
        exercise.put("courseName", courseName);
        
        return exercise;
    }
    
    @Override
    public Map<String, Object> generateExerciseByKnowledgeNames(Long studentId, List<String> knowledgeNames, 
                                                              String difficultyLevel, Integer questionCount) {
        if (studentId == null || knowledgeNames == null || knowledgeNames.isEmpty() || 
                questionCount == null || questionCount <= 0) {
            return new HashMap<>();
        }
        
        // 将知识点名称转换为ID
        List<Long> knowledgeIds = new ArrayList<>();
        for (String name : knowledgeNames) {
            Long id = findKnowledgeIdByName(name);
            if (id != null) {
                knowledgeIds.add(id);
            }
        }
        
        if (knowledgeIds.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "未找到任何有效的知识点");
            return errorResponse;
        }
        
        // 获取第一个知识点所属的课程（实际实现中可能需要更复杂的逻辑）
        Long courseId = findCourseIdByKnowledgeId(knowledgeIds.get(0));
        
        Map<String, Object> exercise = generateExercise(studentId, courseId, knowledgeIds, difficultyLevel, questionCount);
        exercise.put("knowledgeNames", knowledgeNames);
        
        return exercise;
    }
    
    @Override
    public Map<String, Object> generateWeakPointsExercise(Long studentId, Integer questionCount) {
        if (studentId == null || questionCount == null || questionCount <= 0) {
            return new HashMap<>();
        }
        
        // 获取学生的薄弱知识点
        // 实际实现中应该分析学生的学习进度和考试结果，找出掌握度低的知识点
        List<Map<String, Object>> weakPoints = findStudentWeakPoints(studentId);
        
        if (weakPoints.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "没有找到薄弱知识点，建议开始学习新内容");
            return response;
        }
        
        // 提取知识点ID
        List<Long> knowledgeIds = new ArrayList<>();
        List<String> knowledgeNames = new ArrayList<>();
        for (Map<String, Object> point : weakPoints) {
            knowledgeIds.add((Long) point.get("knowledgeId"));
            knowledgeNames.add((String) point.get("knowledgeName"));
        }
        
        // 获取第一个知识点所属的课程
        Long courseId = findCourseIdByKnowledgeId(knowledgeIds.get(0));
        
        Map<String, Object> exercise = generateExercise(studentId, courseId, knowledgeIds, "adaptive", questionCount);
        exercise.put("exerciseType", "weak_points");
        exercise.put("knowledgeNames", knowledgeNames);
        exercise.put("weakPoints", weakPoints);
        
        return exercise;
    }
    
    @Override
    public Map<String, Object> submitExerciseAnswers(Long studentId, String exerciseId, Map<String, String> answers) {
        if (studentId == null || !StringUtils.hasText(exerciseId) || answers == null || answers.isEmpty()) {
            return new HashMap<>();
        }
        
        // 实际实现中，应该检索练习题目并评估答案的正确性
        // 这里简化实现，返回模拟数据
        Map<String, Object> result = new HashMap<>();
        result.put("exerciseId", exerciseId);
        result.put("studentId", studentId);
        result.put("submittedAt", LocalDateTime.now());
        
        int totalQuestions = answers.size();
        int correctCount = 0;
        
        List<Map<String, Object>> questionResults = new ArrayList<>();
        
        for (Map.Entry<String, String> entry : answers.entrySet()) {
            String questionId = entry.getKey();
            String answer = entry.getValue();
            
            Map<String, Object> questionResult = new HashMap<>();
            questionResult.put("questionId", questionId);
            questionResult.put("studentAnswer", answer);
            
            // 模拟评分，随机判断正确或错误
            boolean isCorrect = Math.random() > 0.3; // 70%正确率
            questionResult.put("isCorrect", isCorrect);
            
            if (isCorrect) {
                correctCount++;
                questionResult.put("score", 1);
                questionResult.put("feedback", "回答正确！");
            } else {
                questionResult.put("score", 0);
                questionResult.put("feedback", "回答错误。正确答案是...");
                // 添加解释
                questionResult.put("explanation", "这道题的关键点在于...");
            }
            
            questionResults.add(questionResult);
        }
        
        // 计算得分
        double score = totalQuestions > 0 ? (double) correctCount / totalQuestions * 100 : 0;
        
        result.put("questionResults", questionResults);
        result.put("totalQuestions", totalQuestions);
        result.put("correctCount", correctCount);
        result.put("score", score);
        
        // 生成学习建议
        List<String> suggestions = generateLearningAdvice(score);
        result.put("suggestions", suggestions);
        
        return result;
    }
    
    @Override
    public Map<String, Object> getRealTimeHint(Long studentId, String questionId, String partialAnswer) {
        if (studentId == null || !StringUtils.hasText(questionId)) {
            return new HashMap<>();
        }
        
        // 实际实现中，应该分析学生的部分答案，提供实时提示
        // 这里简化实现，返回模拟数据
        Map<String, Object> hint = new HashMap<>();
        hint.put("questionId", questionId);
        hint.put("studentId", studentId);
        
        if (StringUtils.hasText(partialAnswer)) {
            hint.put("partialAnswer", partialAnswer);
            
            // 简单检查是否有错误关键词
            boolean hasError = partialAnswer.toLowerCase().contains("error") || 
                    partialAnswer.toLowerCase().contains("错误");
            
            if (hasError) {
                hint.put("hasError", true);
                hint.put("errorHint", "您的答案中可能存在错误，请检查...");
            } else {
                hint.put("hasError", false);
            }
        }
        
        // 提供通用提示
        List<String> generalHints = new ArrayList<>();
        generalHints.add("考虑问题的边界条件");
        generalHints.add("思考问题的核心概念是什么");
        generalHints.add("尝试用简单的例子验证您的思路");
        
        hint.put("generalHints", generalHints);
        
        return hint;
    }
    
    @Override
    public List<Map<String, Object>> searchLearningResources(Long studentId, String keywords, 
                                                           String resourceType, Integer limit) {
        if (studentId == null || !StringUtils.hasText(keywords)) {
            return new ArrayList<>();
        }
        
        // 设置默认值
        int actualLimit = limit != null && limit > 0 ? limit : 10;
        
        // 实际实现中，应该根据关键词和资源类型搜索学习资源
        // 这里简化实现，返回模拟数据
        List<Map<String, Object>> resources = new ArrayList<>();
        
        // 生成几个匹配关键词的资源
        for (int i = 0; i < Math.min(actualLimit, 5); i++) {
            Map<String, Object> resource = new HashMap<>();
            resource.put("resourceId", "res-" + UUID.randomUUID().toString().substring(0, 8));
            resource.put("title", keywords + "相关学习资源 " + (i + 1));
            
            // 如果指定了资源类型，则使用指定类型；否则随机分配类型
            if (StringUtils.hasText(resourceType)) {
                resource.put("type", resourceType);
            } else {
                String[] types = {"video", "article", "book", "exercise", "quiz"};
                resource.put("type", types[i % types.length]);
            }
            
            resource.put("url", "https://example.com/resource/" + i);
            resource.put("description", "这是一个关于" + keywords + "的学习资源");
            resource.put("relevance", 0.9 - (i * 0.1)); // 相关度递减
            resources.add(resource);
        }
        
        return resources;
    }
    
    @Override
    public List<Map<String, Object>> getQuestionHistory(Long studentId, Integer limit) {
        if (studentId == null) {
            return new ArrayList<>();
        }
        
        // 设置默认值
        int actualLimit = limit != null && limit > 0 ? limit : 10;
        
        // 实际实现中，应该从数据库查询学生的历史问答记录
        // 这里简化实现，返回模拟数据
        List<Map<String, Object>> history = new ArrayList<>();
        
        // 生成一些历史问答记录
        for (int i = 0; i < Math.min(actualLimit, 5); i++) {
            Map<String, Object> record = new HashMap<>();
            record.put("questionId", "q-history-" + i);
            record.put("studentId", studentId);
            record.put("question", "历史问题示例 " + (i + 1));
            record.put("answer", "这是对历史问题" + (i + 1) + "的回答内容...");
            record.put("timestamp", LocalDateTime.now().minusDays(i));
            
            // 随机添加课程信息
            if (i % 2 == 0) {
                record.put("courseId", (long) (i + 1));
                record.put("courseName", "课程" + (i + 1));
            }
            
            history.add(record);
        }
        
        return history;
    }
    
    @Override
    public List<Map<String, Object>> searchQuestionHistory(Long studentId, String keywords, Integer limit) {
        if (studentId == null || !StringUtils.hasText(keywords)) {
            return new ArrayList<>();
        }
        
        // 设置默认值
        int actualLimit = limit != null && limit > 0 ? limit : 10;
        
        // 实际实现中，应该根据关键词搜索学生的历史问答记录
        // 这里简化实现，返回模拟数据
        List<Map<String, Object>> history = new ArrayList<>();
        
        // 生成一些匹配关键词的历史问答记录
        for (int i = 0; i < Math.min(actualLimit, 3); i++) {
            Map<String, Object> record = new HashMap<>();
            record.put("questionId", "q-search-" + i);
            record.put("studentId", studentId);
            record.put("question", "包含关键词\"" + keywords + "\"的历史问题 " + (i + 1));
            record.put("answer", "这是对包含关键词\"" + keywords + "\"的问题" + (i + 1) + "的回答...");
            record.put("timestamp", LocalDateTime.now().minusDays(i * 2));
            record.put("relevance", 0.9 - (i * 0.2)); // 相关度递减
            
            history.add(record);
        }
        
        return history;
    }
    
    /**
     * 生成问题ID
     * @return 问题ID
     */
    private String generateQuestionId() {
        return "q-" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * 生成练习ID
     * @return 练习ID
     */
    private String generateExerciseId() {
        return "ex-" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * 根据问题生成示例回答
     * @param question 问题内容
     * @return 回答内容
     */
    private String generateSampleAnswer(String question) {
        // 在实际实现中，这里应该调用AI服务或知识库来生成回答
        if (question.toLowerCase().contains("java")) {
            return "Java是一种广泛使用的计算机编程语言，拥有跨平台、面向对象、泛型编程的特性。Java的语法很多来源于C和C++，但有更高级的特性。";
        } else if (question.toLowerCase().contains("数据库") || question.toLowerCase().contains("database")) {
            return "数据库是按照数据结构来组织、存储和管理数据的仓库。常见的数据库类型包括关系型数据库（如MySQL、Oracle）和非关系型数据库（如MongoDB、Redis）。";
        } else {
            return "您的问题很有趣。在学习过程中，理解基本概念和原理是非常重要的。建议您结合实例和练习来深化理解。";
        }
    }
    
    /**
     * 生成示例资源
     * @return 示例资源列表
     */
    private List<Map<String, Object>> generateSampleResources() {
        List<Map<String, Object>> resources = new ArrayList<>();
        
        Map<String, Object> resource1 = new HashMap<>();
        resource1.put("resourceId", "res-sample-1");
        resource1.put("title", "入门教程");
        resource1.put("type", "article");
        resource1.put("url", "https://example.com/tutorial");
        resources.add(resource1);
        
        Map<String, Object> resource2 = new HashMap<>();
        resource2.put("resourceId", "res-sample-2");
        resource2.put("title", "视频讲解");
        resource2.put("type", "video");
        resource2.put("url", "https://example.com/video-tutorial");
        resources.add(resource2);
        
        return resources;
    }
    
    /**
     * 生成示例题目
     * @param count 题目数量
     * @return 题目列表
     */
    private List<Map<String, Object>> generateSampleQuestions(int count) {
        List<Map<String, Object>> questions = new ArrayList<>();
        
        String[] questionTypes = {"multiple_choice", "true_false", "short_answer", "fill_blank"};
        
        for (int i = 0; i < count; i++) {
            Map<String, Object> question = new HashMap<>();
            String questionId = "q-" + UUID.randomUUID().toString().substring(0, 8);
            String type = questionTypes[i % questionTypes.length];
            
            question.put("questionId", questionId);
            question.put("type", type);
            question.put("content", "这是第" + (i + 1) + "道题目，类型为" + type);
            
            // 根据题目类型添加不同的附加信息
            if ("multiple_choice".equals(type)) {
                List<String> options = new ArrayList<>();
                options.add("选项A");
                options.add("选项B");
                options.add("选项C");
                options.add("选项D");
                question.put("options", options);
                question.put("correctAnswer", "A"); // 实际实现中应该根据题目内容确定正确答案
            } else if ("true_false".equals(type)) {
                question.put("correctAnswer", "true");
            } else if ("fill_blank".equals(type)) {
                question.put("blanks", 1); // 填空数量
                question.put("correctAnswer", "正确答案"); // 实际实现中应该根据题目内容确定正确答案
            }
            
            question.put("score", 1); // 每题1分
            questions.add(question);
        }
        
        return questions;
    }
    
    /**
     * 查找学生的薄弱知识点
     * @param studentId 学生ID
     * @return 薄弱知识点列表
     */
    private List<Map<String, Object>> findStudentWeakPoints(Long studentId) {
        // 在实际实现中，应该分析学生的学习进度和考试结果，找出掌握度低的知识点
        // 这里简化实现，返回模拟数据
        List<Map<String, Object>> weakPoints = new ArrayList<>();
        
        Map<String, Object> point1 = new HashMap<>();
        point1.put("knowledgeId", 101L);
        point1.put("knowledgeName", "数据结构");
        point1.put("masteryLevel", 0.35);
        weakPoints.add(point1);
        
        Map<String, Object> point2 = new HashMap<>();
        point2.put("knowledgeId", 102L);
        point2.put("knowledgeName", "算法复杂度");
        point2.put("masteryLevel", 0.42);
        weakPoints.add(point2);
        
        return weakPoints;
    }
    
    /**
     * 根据得分生成学习建议
     * @param score 得分
     * @return 学习建议列表
     */
    private List<String> generateLearningAdvice(double score) {
        List<String> advice = new ArrayList<>();
        
        if (score >= 90) {
            advice.add("优秀！你已经很好地掌握了这些知识点。");
            advice.add("建议尝试更高难度的练习来挑战自己。");
        } else if (score >= 70) {
            advice.add("不错的表现！你对大部分内容有良好的理解。");
            advice.add("建议回顾错题，加深对这些知识点的理解。");
        } else if (score >= 50) {
            advice.add("基础已经掌握，但还需要更多练习。");
            advice.add("建议重新学习相关章节，然后再做类似的练习。");
        } else {
            advice.add("需要加强基础知识的学习。");
            advice.add("建议从基础概念开始，系统地复习这部分内容。");
            advice.add("可以寻求老师或同学的帮助，解决你的疑惑。");
        }
        
        return advice;
    }
    
    /**
     * 通过课程名称查找课程ID
     * @param courseName 课程名称
     * @return 课程ID
     */
    private Long findCourseIdByName(String courseName) {
        // 在实际实现中，应该查询数据库
        // 这里简化实现，返回模拟数据
        if ("Java编程".equals(courseName)) {
            return 1L;
        } else if ("数据库原理".equals(courseName)) {
            return 2L;
        } else if ("数据结构".equals(courseName)) {
            return 3L;
        }
        return 1L; // 默认返回ID为1的课程
    }
    
    /**
     * 通过知识点名称查找知识点ID
     * @param knowledgeName 知识点名称
     * @return 知识点ID
     */
    private Long findKnowledgeIdByName(String knowledgeName) {
        // 在实际实现中，应该查询数据库
        // 这里简化实现，返回模拟数据
        if ("Java基础语法".equals(knowledgeName)) {
            return 101L;
        } else if ("面向对象编程".equals(knowledgeName)) {
            return 102L;
        } else if ("SQL语句".equals(knowledgeName)) {
            return 201L;
        }
        return 101L; // 默认返回ID为101的知识点
    }
    
    /**
     * 通过知识点ID查找所属课程ID
     * @param knowledgeId 知识点ID
     * @return 课程ID
     */
    private Long findCourseIdByKnowledgeId(Long knowledgeId) {
        // 在实际实现中，应该查询数据库
        // 这里简化实现，返回模拟数据
        if (knowledgeId != null) {
            if (knowledgeId >= 100 && knowledgeId < 200) {
                return 1L; // Java编程课程
            } else if (knowledgeId >= 200 && knowledgeId < 300) {
                return 2L; // 数据库原理课程
            }
        }
        return 1L; // 默认返回ID为1的课程
    }
} 