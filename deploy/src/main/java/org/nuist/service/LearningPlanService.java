package org.nuist.service;

import org.nuist.bo.LearningPlanBO;

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

     * @return 学习计划
     */
    Map<String, Object> generateLearningPlan(Long studentId, String targetGoal, Integer timeFrame, 
                                           Long courseId);
    

    /**
     * 获取学生的当前学习计划
     * @param studentId 学生ID
     * @return 当前学习计划
     */
    List<LearningPlanBO> getCurrentLearningPlan(Long studentId);
    
    /**
     * 获取学生的历史学习计划
     * @param studentId 学生ID
     * @return 历史学习计划列表
     */
    List<LearningPlanBO> getLearningPlanHistory(Long studentId);
    
    /**
     * 根据关键词搜索学习计划
     * @param studentId 学生ID
     * @param keywords 关键词
     * @return 匹配的学习计划列表
     */
    List<LearningPlanBO> searchLearningPlans(Long studentId, String keywords);
    

    /**
     * 获取特定日期的学习计划内容
     * @param studentId 学生ID
     * @param date 日期
     * @return 指定日期的学习计划内容
     */
    List<LearningPlanBO> getDailyPlanActivities(Long studentId, LocalDate date);
    
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

    LearningPlanBO updatePlan(LearningPlanBO learningPlanBO);


    List<LearningPlanBO> getifCompletedLearningPlans(Long studentId, boolean b);

    boolean deletePlan(Long planId);

    LearningPlanBO addPlan(LearningPlanBO learningPlanBO);
} 