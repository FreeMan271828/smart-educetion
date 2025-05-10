package org.nuist.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 个性化学习计划服务接口
 */
public interface LearningPlanService {
    
    /**
     * 生成个性化学习计划
     * @param studentId 学生ID
     * @param targetGoal 学习目标
     * @param timeFrame 时间框架(天)
     * @param courseIds 课程ID列表(可选)
     * @param knowledgeIds 重点知识点ID列表(可选)
     * @return 学习计划
     */
    Map<String, Object> generateLearningPlan(Long studentId, String targetGoal, Integer timeFrame, 
                                           List<Long> courseIds, List<Long> knowledgeIds);
    
    /**
     * 根据课程名称生成学习计划
     * @param studentId 学生ID
     * @param targetGoal 学习目标
     * @param timeFrame 时间框架(天)
     * @param courseNames 课程名称列表
     * @return 学习计划
     */
    Map<String, Object> generateLearningPlanByCourseName(Long studentId, String targetGoal, 
                                                       Integer timeFrame, List<String> courseNames);
    
    /**
     * 根据知识点名称生成学习计划
     * @param studentId 学生ID
     * @param targetGoal 学习目标
     * @param timeFrame 时间框架(天)
     * @param knowledgeNames 知识点名称列表
     * @return 学习计划
     */
    Map<String, Object> generateLearningPlanByKnowledgeName(Long studentId, String targetGoal, 
                                                          Integer timeFrame, List<String> knowledgeNames);
    
    /**
     * 获取学生的当前学习计划
     * @param studentId 学生ID
     * @return 当前学习计划
     */
    Map<String, Object> getCurrentLearningPlan(Long studentId);
    
    /**
     * 获取学生的历史学习计划
     * @param studentId 学生ID
     * @return 历史学习计划列表
     */
    List<Map<String, Object>> getLearningPlanHistory(Long studentId);
    
    /**
     * 根据关键词搜索学习计划
     * @param studentId 学生ID
     * @param keywords 关键词
     * @return 匹配的学习计划列表
     */
    List<Map<String, Object>> searchLearningPlans(Long studentId, String keywords);
    
    /**
     * 更新学习计划进度
     * @param planId 计划ID
     * @param activityId 活动ID
     * @param status 完成状态
     * @param feedback 反馈信息(可选)
     * @return 更新后的计划详情
     */
    Map<String, Object> updatePlanProgress(String planId, String activityId, String status, String feedback);
    
    /**
     * 获取特定日期的学习计划内容
     * @param studentId 学生ID
     * @param date 日期
     * @return 指定日期的学习计划内容
     */
    List<Map<String, Object>> getDailyPlanActivities(Long studentId, LocalDate date);
    
    /**
     * 根据计划名称和日期获取学习计划内容
     * @param studentId 学生ID
     * @param planName 计划名称
     * @param date 日期
     * @return 学习计划内容
     */
    List<Map<String, Object>> getDailyPlanByName(Long studentId, String planName, LocalDate date);
    
    /**
     * 获取学习计划推荐资源
     * @param planId 计划ID
     * @return 推荐资源列表
     */
    List<Map<String, Object>> getPlanRecommendedResources(String planId);
    
    /**
     * 根据计划名称获取推荐资源
     * @param studentId 学生ID
     * @param planName 计划名称
     * @return 推荐资源列表
     */
    List<Map<String, Object>> getPlanResourcesByName(Long studentId, String planName);
    
    /**
     * 根据关键词搜索推荐资源
     * @param studentId 学生ID
     * @param keywords 关键词
     * @return 匹配的资源列表
     */
    List<Map<String, Object>> searchPlanResources(Long studentId, String keywords);
} 