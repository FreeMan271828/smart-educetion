package org.nuist.service;

import org.nuist.bo.StudentExamAnswerBO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 学生考试服务接口
 */
public interface StudentExamService {
    
    /**
     * 获取学生的考试答案
     * @param studentId 学生ID
     * @param examId 考试ID
     * @return 考试答案列表
     */
    List<StudentExamAnswerBO> getStudentExamAnswers(Long studentId, Long examId);
    
    /**
     * 根据考试标题模糊查询学生的考试答案
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 考试答案列表
     */
    List<StudentExamAnswerBO> getStudentExamAnswersByTitle(Long studentId, String examTitle);
    
    /**
     * 获取特定题目的答案
     * @param studentId 学生ID
     * @param examId 考试ID
     * @param questionId 问题ID
     * @return 考试答案
     */
    StudentExamAnswerBO getStudentQuestionAnswer(Long studentId, Long examId, Long questionId);
    
    /**
     * 根据问题内容模糊查询学生的答案
     * @param studentId 学生ID
     * @param examId 考试ID
     * @param questionContent 问题内容关键词
     * @return 考试答案列表
     */
    List<StudentExamAnswerBO> getStudentAnswersByQuestionContent(Long studentId, Long examId, String questionContent);
    
    /**
     * 提交考试答案
     * @param answerBO 考试答案业务对象
     * @return 保存的答案ID
     */
    Long submitAnswer(StudentExamAnswerBO answerBO);
    
    /**
     * 批量提交考试答案
     * @param answerList 答案列表
     * @return 成功提交的数量
     */
    int batchSubmitAnswers(List<StudentExamAnswerBO> answerList);
    
    /**
     * 获取考试得分
     * @param studentId 学生ID
     * @param examId 考试ID
     * @return 总得分
     */
    BigDecimal getExamScore(Long studentId, Long examId);
    
    /**
     * 根据考试标题获取考试得分
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 总得分
     */
    BigDecimal getExamScoreByTitle(Long studentId, String examTitle);
    
    /**
     * 获取学生所有考试的得分情况
     * @param studentId 学生ID
     * @return 考试得分情况列表
     */
    List<Map<String, Object>> getStudentExamScores(Long studentId);
    
    /**
     * 根据考试标题关键词搜索学生的考试得分情况
     * @param studentId 学生ID
     * @param titleKeywords 标题关键词
     * @return 考试得分情况列表
     */
    List<Map<String, Object>> searchStudentExamScores(Long studentId, String titleKeywords);
    
    /**
     * 获取学生在特定考试中的详细情况
     * @param studentId 学生ID
     * @param examId 考试ID
     * @return 详细情况(包括总分、每题得分、排名等)
     */
    Map<String, Object> getExamDetail(Long studentId, Long examId);
    
    /**
     * 根据考试标题获取详细情况
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 详细情况
     */
    Map<String, Object> getExamDetailByTitle(Long studentId, String examTitle);
    
    /**
     * 分析考试结果，生成知识点掌握情况
     * @param studentId 学生ID
     * @param examId 考试ID
     * @return 知识点掌握情况
     */
    Map<String, Object> analyzeExamResult(Long studentId, Long examId);
    
    /**
     * 根据考试标题分析考试结果
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 知识点掌握情况
     */
    Map<String, Object> analyzeExamResultByTitle(Long studentId, String examTitle);
    
    /**
     * 智能评测学生答案(针对主观题)
     * @param answerId 答案ID
     * @return 评测结果(包括得分、反馈等)
     */
    Map<String, Object> intelligentEvaluateAnswer(Long answerId);
    
    /**
     * 根据学生ID、考试标题和问题内容智能评测答案
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @param questionContent 问题内容关键词
     * @return 评测结果列表
     */
    List<Map<String, Object>> intelligentEvaluateAnswerByContent(Long studentId, String examTitle, String questionContent);
    
    /**
     * 根据考试结果生成学习建议
     * @param studentId 学生ID
     * @param examId 考试ID
     * @return 学习建议
     */
    List<String> generateLearningAdvice(Long studentId, Long examId);
    
    /**
     * 根据考试标题生成学习建议
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 学习建议
     */
    List<String> generateLearningAdviceByTitle(Long studentId, String examTitle);
    
    /**
     * 根据关键词搜索考试和问题
     * @param studentId 学生ID
     * @param keywords 关键词(可匹配考试标题、问题内容等)
     * @return 匹配的考试和问题信息
     */
    Map<String, Object> searchExamsAndQuestions(Long studentId, String keywords);
} 