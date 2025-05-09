package org.nuist.service;

import org.nuist.business_object.LearningProgressBO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 学习进度服务接口
 */
public interface LearningProgressService {
    
    /**
     * 获取学生的学习进度
     * @param studentId 学生ID
     * @return 学习进度列表
     */
    List<LearningProgressBO> getStudentProgress(Long studentId);
    
    /**
     * 获取学生在某课程下的学习进度
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 学习进度列表
     */
    List<LearningProgressBO> getStudentCourseProgress(Long studentId, Long courseId);
    
    /**
     * 根据课程名称模糊查询学习进度
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 学习进度列表
     */
    List<LearningProgressBO> getStudentCourseProgressByName(Long studentId, String courseName);
    
    /**
     * 获取学生对特定知识点的学习进度
     * @param studentId 学生ID
     * @param knowledgeId 知识点ID
     * @return 学习进度业务对象
     */
    LearningProgressBO getStudentKnowledgeProgress(Long studentId, Long knowledgeId);
    
    /**
     * 根据知识点名称模糊查询学习进度
     * @param studentId 学生ID
     * @param knowledgeName 知识点名称
     * @return 学习进度列表
     */
    List<LearningProgressBO> getStudentKnowledgeProgressByName(Long studentId, String knowledgeName);
    
    /**
     * 更新学习进度
     * @param progressBO 学习进度业务对象
     * @return 更新后的学习进度ID
     */
    Long updateLearningProgress(LearningProgressBO progressBO);
    
    /**
     * 批量更新学习进度
     * @param progressList 学习进度列表
     * @return 更新成功的数量
     */
    int batchUpdateLearningProgress(List<LearningProgressBO> progressList);
    
    /**
     * 根据新的学习结果更新掌握度
     * @param studentId 学生ID
     * @param knowledgeId 知识点ID
     * @param score 新得分
     * @return 更新后的学习进度
     */
    LearningProgressBO updateMasteryLevel(Long studentId, Long knowledgeId, BigDecimal score);
    
    /**
     * 根据知识点名称更新掌握度
     * @param studentId 学生ID
     * @param knowledgeName 知识点名称
     * @param score 新得分
     * @return 更新后的学习进度
     */
    LearningProgressBO updateMasteryLevelByName(Long studentId, String knowledgeName, BigDecimal score);
    
    /**
     * 获取学生的总体学习进度百分比
     * @param studentId 学生ID
     * @return 总体学习进度百分比
     */
    BigDecimal getOverallProgress(Long studentId);
    
    /**
     * 获取学生课程的总体学习进度百分比
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 课程学习进度百分比
     */
    BigDecimal getCourseProgress(Long studentId, Long courseId);
    
    /**
     * 根据课程名称获取学习进度百分比
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 课程学习进度百分比
     */
    BigDecimal getCourseProgressByName(Long studentId, String courseName);
    
    /**
     * 获取学生的学习进度统计
     * @param studentId 学生ID
     * @return 进度统计信息(包括已掌握、学习中、未学习的知识点数量等)
     */
    Map<String, Object> getProgressStatistics(Long studentId);
    
    /**
     * 根据课程名称获取学习进度统计
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 进度统计信息
     */
    Map<String, Object> getProgressStatisticsByCourseName(Long studentId, String courseName);
    
    /**
     * 生成个性化学习计划
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @param days 计划天数
     * @return 学习计划(Map结构，包含每日学习内容)
     */
    Map<String, Object> generateLearningPlan(Long studentId, Long courseId, Integer days);
    
    /**
     * 根据课程名称生成个性化学习计划
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @param days 计划天数
     * @return 学习计划
     */
    Map<String, Object> generateLearningPlanByCourseName(Long studentId, String courseName, Integer days);
    
    /**
     * 获取推荐学习资源
     * @param studentId 学生ID
     * @param limit 数量限制
     * @return 推荐资源列表
     */
    List<Map<String, Object>> getRecommendedResources(Long studentId, Integer limit);
    
    /**
     * 根据关键词获取推荐学习资源
     * @param studentId 学生ID
     * @param keywords 关键词
     * @param limit 数量限制
     * @return 推荐资源列表
     */
    List<Map<String, Object>> searchRecommendedResources(Long studentId, String keywords, Integer limit);
} 