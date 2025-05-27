package org.nuist.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nuist.po.LearningProgressPO;

import java.util.List;
import java.util.Map;

/**
 * 学习进度数据访问接口
 */
@Mapper
public interface LearningProgressMapper extends BaseMapper<LearningProgressPO> {
    
    /**
     * 根据学生ID查询学习进度
     * @param studentId 学生ID
     * @return 学习进度PO列表
     */
    List<LearningProgressPO> selectByStudentId(@Param("studentId") Long studentId);
    
    /**
     * 根据学生ID和课程ID查询学习进度
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 学习进度PO列表
     */
    List<LearningProgressPO> selectByStudentIdAndCourseId(
            @Param("studentId") Long studentId, 
            @Param("courseId") Long courseId);
    
    /**
     * 根据学生ID和课程名称查询学习进度
     * @param studentId 学生ID
     * @param courseNameLike 课程名称模糊匹配
     * @return 学习进度PO列表
     */
    List<LearningProgressPO> selectByStudentIdAndCourseName(
            @Param("studentId") Long studentId, 
            @Param("courseNameLike") String courseNameLike);
    
    /**
     * 根据学生ID和知识点ID查询学习进度
     * @param studentId 学生ID
     * @param knowledgeId 知识点ID
     * @return 学习进度PO
     */
    LearningProgressPO selectByStudentIdAndKnowledgeId(
            @Param("studentId") Long studentId, 
            @Param("knowledgeId") Long knowledgeId);
    
    /**
     * 根据学生ID和知识点名称查询学习进度
     * @param studentId 学生ID
     * @param knowledgeNameLike 知识点名称模糊匹配
     * @return 学习进度PO列表
     */
    List<LearningProgressPO> selectByStudentIdAndKnowledgeName(
            @Param("studentId") Long studentId, 
            @Param("knowledgeNameLike") String knowledgeNameLike);
    
    /**
     * 统计学生已掌握的知识点数量
     * @param studentId 学生ID
     * @return 已掌握知识点数量
     */
    int countMasteredKnowledgePoints(@Param("studentId") Long studentId);
    
    /**
     * 统计学生学习中的知识点数量
     * @param studentId 学生ID
     * @return 学习中知识点数量
     */
    int countInProgressKnowledgePoints(@Param("studentId") Long studentId);
    
    /**
     * 统计学生在特定课程中已掌握的知识点数量
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 已掌握知识点数量
     */
    int countMasteredKnowledgePointsByCourse(
            @Param("studentId") Long studentId, 
            @Param("courseId") Long courseId);
    
    /**
     * 统计学生在特定课程中学习中的知识点数量
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 学习中知识点数量
     */
    int countInProgressKnowledgePointsByCourse(
            @Param("studentId") Long studentId, 
            @Param("courseId") Long courseId);
    
    /**
     * 查询学生在特定课程中未掌握的知识点
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 未掌握知识点信息列表
     */
    List<Map<String, Object>> selectNotMasteredKnowledgePointsByCourse(
            @Param("studentId") Long studentId, 
            @Param("courseId") Long courseId);
    
    /**
     * 查询推荐给学生的学习资源
     * @param studentId 学生ID
     * @param limit 数量限制
     * @return 推荐资源列表
     */
    List<Map<String, Object>> selectRecommendedResourcesForStudent(
            @Param("studentId") Long studentId, 
            @Param("limit") int limit);
    
    /**
     * 根据关键词搜索推荐资源
     * @param studentId 学生ID
     * @param keywordsLike 关键词模糊匹配
     * @param limit 数量限制
     * @return 推荐资源列表
     */
    List<Map<String, Object>> searchRecommendedResourcesForStudent(
            @Param("studentId") Long studentId, 
            @Param("keywordsLike") String keywordsLike, 
            @Param("limit") int limit);
} 