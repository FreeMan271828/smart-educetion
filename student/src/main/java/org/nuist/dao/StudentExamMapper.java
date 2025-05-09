package org.nuist.dao;

import org.nuist.persist_object.StudentExamAnswerPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 学生考试答案数据访问接口
 */
@Mapper
public interface StudentExamMapper extends BaseMapper<StudentExamAnswerPO> {
    
    /**
     * 根据学生ID和考试标题模糊查询答案
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 答案列表
     */
    List<StudentExamAnswerPO> selectByStudentAndExamTitle(@Param("studentId") Long studentId, @Param("examTitle") String examTitle);

    
    /**
     * 根据学生ID、考试ID和问题内容关键词查询答案
     * @param studentId 学生ID
     * @param examId 考试ID
     * @param questionContent 问题内容关键词
     * @return 答案列表
     */
    List<StudentExamAnswerPO> selectByStudentAndQuestionContent(
            @Param("studentId") Long studentId, 
            @Param("examId") Long examId, 
            @Param("questionContent") String questionContent);

    
    /**
     * 计算学生在某考试的总分
     * @param studentId 学生ID
     * @param examId 考试ID
     * @return 总分
     */
    BigDecimal sumScoreByStudentAndExam(@Param("studentId") Long studentId, @Param("examId") Long examId);
    
    /**
     * 根据考试标题计算学生在某考试的总分
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 总分
     */
    BigDecimal sumScoreByStudentAndExamTitle(@Param("studentId") Long studentId, @Param("examTitle") String examTitle);
    
    /**
     * 获取学生所有考试的得分情况
     * @param studentId 学生ID
     * @return 考试得分情况列表
     */
    List<Map<String, Object>> selectExamScoresByStudent(@Param("studentId") Long studentId);
    
    /**
     * 根据标题关键词搜索学生的考试得分情况
     * @param studentId 学生ID
     * @param titleKeywords 标题关键词
     * @return 考试得分情况列表
     */
    List<Map<String, Object>> searchExamScoresByTitleKeywords(
            @Param("studentId") Long studentId, 
            @Param("titleKeywords") String titleKeywords);
    
    /**
     * 获取考试详情
     * @param studentId 学生ID
     * @param examId 考试ID
     * @return 考试详情
     */
    Map<String, Object> selectExamDetail(@Param("studentId") Long studentId, @Param("examId") Long examId);
    
    /**
     * 根据考试标题获取考试详情
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 考试详情
     */
    Map<String, Object> selectExamDetailByTitle(@Param("studentId") Long studentId, @Param("examTitle") String examTitle);
    
    /**
     * 查询学生在某考试中的知识点掌握情况
     * @param studentId 学生ID
     * @param examId 考试ID
     * @return 知识点掌握情况
     */
    List<Map<String, Object>> analyzeKnowledgePoints(@Param("studentId") Long studentId, @Param("examId") Long examId);
    
    /**
     * 根据考试标题查询知识点掌握情况
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 知识点掌握情况
     */
    List<Map<String, Object>> analyzeKnowledgePointsByTitle(
            @Param("studentId") Long studentId, 
            @Param("examTitle") String examTitle);
} 