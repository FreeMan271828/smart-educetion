package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nuist.business_object.StudentExamAnswerBO;
import org.nuist.dao.StudentExamMapper;
import org.nuist.persist_object.StudentExamAnswerPO;
import org.nuist.service.StudentExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 学生考试服务实现类
 */
@Service
public class StudentExamServiceImpl extends ServiceImpl<StudentExamMapper, StudentExamAnswerPO> implements StudentExamService {
    
    @Autowired
    private StudentExamMapper studentExamMapper;
    
    @Override
    public List<StudentExamAnswerBO> getStudentExamAnswers(Long studentId, Long examId) {
        if (studentId == null || examId == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<StudentExamAnswerPO> queryWrapper = Wrappers.<StudentExamAnswerPO>lambdaQuery()
                .eq(StudentExamAnswerPO::getStudentId, studentId)
                .eq(StudentExamAnswerPO::getExamId, examId);
        
        List<StudentExamAnswerPO> poList = list(queryWrapper);
        return convertToBOList(poList);
    }
    
    @Override
    public List<StudentExamAnswerBO> getStudentExamAnswersByTitle(Long studentId, String examTitle) {
        if (studentId == null || !StringUtils.hasText(examTitle)) {
            return new ArrayList<>();
        }
        
        List<StudentExamAnswerPO> poList = studentExamMapper.selectByStudentAndExamTitle(studentId, examTitle);
        return convertToBOList(poList);
    }
    
    @Override
    public StudentExamAnswerBO getStudentQuestionAnswer(Long studentId, Long examId, Long questionId) {
        if (studentId == null || examId == null || questionId == null) {
            return null;
        }
        
        LambdaQueryWrapper<StudentExamAnswerPO> queryWrapper = Wrappers.<StudentExamAnswerPO>lambdaQuery()
                .eq(StudentExamAnswerPO::getStudentId, studentId)
                .eq(StudentExamAnswerPO::getExamId, examId)
                .eq(StudentExamAnswerPO::getQuestionId, questionId);
        
        StudentExamAnswerPO po = getOne(queryWrapper);
        return po != null ? StudentExamAnswerBO.fromPO(po) : null;
    }
    
    @Override
    public List<StudentExamAnswerBO> getStudentAnswersByQuestionContent(Long studentId, Long examId, String questionContent) {
        if (studentId == null || examId == null || !StringUtils.hasText(questionContent)) {
            return new ArrayList<>();
        }
        
        List<StudentExamAnswerPO> poList = studentExamMapper.selectByStudentAndQuestionContent(studentId, examId, questionContent);
        return convertToBOList(poList);
    }
    
    @Override
    @Transactional
    public Long submitAnswer(StudentExamAnswerBO answerBO) {
        if (answerBO == null || answerBO.getStudentId() == null || 
                answerBO.getExamId() == null || answerBO.getQuestionId() == null) {
            return null;
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (answerBO.getCreatedAt() == null) {
            answerBO.setCreatedAt(now);
        }
        answerBO.setUpdatedAt(now);
        
        StudentExamAnswerPO po = answerBO.toPO();
        saveOrUpdate(po);
        
        return po.getAnswerId();
    }
    
    @Override
    @Transactional
    public int batchSubmitAnswers(List<StudentExamAnswerBO> answerList) {
        if (answerList == null || answerList.isEmpty()) {
            return 0;
        }
        
        LocalDateTime now = LocalDateTime.now();
        List<StudentExamAnswerPO> poList = answerList.stream()
                .filter(bo -> bo != null && bo.getStudentId() != null && 
                        bo.getExamId() != null && bo.getQuestionId() != null)
                .map(bo -> {
                    if (bo.getCreatedAt() == null) {
                        bo.setCreatedAt(now);
                    }
                    bo.setUpdatedAt(now);
                    return bo.toPO();
                })
                .collect(Collectors.toList());
        
        if (poList.isEmpty()) {
            return 0;
        }
        
        saveBatch(poList);
        return poList.size();
    }
    
    @Override
    public BigDecimal getExamScore(Long studentId, Long examId) {
        if (studentId == null || examId == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal totalScore = studentExamMapper.sumScoreByStudentAndExam(studentId, examId);
        return totalScore != null ? totalScore : BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getExamScoreByTitle(Long studentId, String examTitle) {
        if (studentId == null || !StringUtils.hasText(examTitle)) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal totalScore = studentExamMapper.sumScoreByStudentAndExamTitle(studentId, examTitle);
        return totalScore != null ? totalScore : BigDecimal.ZERO;
    }
    
    @Override
    public List<Map<String, Object>> getStudentExamScores(Long studentId) {
        if (studentId == null) {
            return new ArrayList<>();
        }
        
        return studentExamMapper.selectExamScoresByStudent(studentId);
    }
    
    @Override
    public List<Map<String, Object>> searchStudentExamScores(Long studentId, String titleKeywords) {
        if (studentId == null || !StringUtils.hasText(titleKeywords)) {
            return new ArrayList<>();
        }
        
        return studentExamMapper.searchExamScoresByTitleKeywords(studentId, titleKeywords);
    }
    
    @Override
    public Map<String, Object> getExamDetail(Long studentId, Long examId) {
        if (studentId == null || examId == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> detail = studentExamMapper.selectExamDetail(studentId, examId);
        return detail != null ? detail : new HashMap<>();
    }
    
    @Override
    public Map<String, Object> getExamDetailByTitle(Long studentId, String examTitle) {
        if (studentId == null || !StringUtils.hasText(examTitle)) {
            return new HashMap<>();
        }
        
        Map<String, Object> detail = studentExamMapper.selectExamDetailByTitle(studentId, examTitle);
        return detail != null ? detail : new HashMap<>();
    }
    
    @Override
    public Map<String, Object> analyzeExamResult(Long studentId, Long examId) {
        if (studentId == null || examId == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> knowledgePoints = studentExamMapper.analyzeKnowledgePoints(studentId, examId);
        
        result.put("studentId", studentId);
        result.put("examId", examId);
        result.put("totalScore", getExamScore(studentId, examId));
        result.put("knowledgePoints", knowledgePoints);
        
        return result;
    }
    
    @Override
    public Map<String, Object> analyzeExamResultByTitle(Long studentId, String examTitle) {
        if (studentId == null || !StringUtils.hasText(examTitle)) {
            return new HashMap<>();
        }
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> knowledgePoints = studentExamMapper.analyzeKnowledgePointsByTitle(studentId, examTitle);
        
        result.put("studentId", studentId);
        result.put("examTitle", examTitle);
        result.put("totalScore", getExamScoreByTitle(studentId, examTitle));
        result.put("knowledgePoints", knowledgePoints);
        
        return result;
    }
    
    @Override
    public Map<String, Object> intelligentEvaluateAnswer(Long answerId) {
        if (answerId == null) {
            return new HashMap<>();
        }
        
        StudentExamAnswerPO po = getById(answerId);
        if (po == null) {
            return new HashMap<>();
        }
        
        // 在实际实现中，这里可能需要调用外部AI服务或其他评分服务
        // 这里仅做模拟实现
        Map<String, Object> result = new HashMap<>();
        result.put("answerId", answerId);
        result.put("score", po.getScore());
        result.put("feedback", po.getFeedback());
        result.put("aiSuggestion", "这是一个智能评测建议。实际项目中可能需要集成OpenAI等AI服务。");
        
        return result;
    }
    
    @Override
    public List<Map<String, Object>> intelligentEvaluateAnswerByContent(Long studentId, String examTitle, String questionContent) {
        if (studentId == null || !StringUtils.hasText(examTitle) || !StringUtils.hasText(questionContent)) {
            return new ArrayList<>();
        }
        
        // 查找相关答案
        List<StudentExamAnswerPO> answerList = studentExamMapper.selectByStudentAndQuestionContent(studentId, null, questionContent);
        
        // 对每个答案进行智能评测
        List<Map<String, Object>> resultList = answerList.stream()
                .map(po -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("answerId", po.getAnswerId());
                    result.put("questionId", po.getQuestionId());
                    result.put("studentAnswer", po.getStudentAnswer());
                    result.put("score", po.getScore());
                    result.put("feedback", po.getFeedback());
                    result.put("aiSuggestion", "这是一个智能评测建议。实际项目中可能需要集成OpenAI等AI服务。");
                    return result;
                })
                .collect(Collectors.toList());
        
        return resultList;
    }
    
    @Override
    public List<String> generateLearningAdvice(Long studentId, Long examId) {
        if (studentId == null || examId == null) {
            return new ArrayList<>();
        }
        
        // 分析考试结果
        Map<String, Object> analysis = analyzeExamResult(studentId, examId);
        
        // 根据分析生成学习建议
        // 实际项目中可能需要更复杂的逻辑或AI服务
        List<String> adviceList = new ArrayList<>();
        adviceList.add("根据考试结果，建议加强某某知识点的学习");
        adviceList.add("可以尝试做更多相关练习题来提高掌握程度");
        adviceList.add("建议参考教材第X章第Y节的内容进行复习");
        
        return adviceList;
    }
    
    @Override
    public List<String> generateLearningAdviceByTitle(Long studentId, String examTitle) {
        if (studentId == null || !StringUtils.hasText(examTitle)) {
            return new ArrayList<>();
        }
        
        // 分析考试结果
        Map<String, Object> analysis = analyzeExamResultByTitle(studentId, examTitle);
        
        // 根据分析生成学习建议
        List<String> adviceList = new ArrayList<>();
        adviceList.add("根据" + examTitle + "考试结果，建议加强某某知识点的学习");
        adviceList.add("可以尝试做更多相关练习题来提高掌握程度");
        adviceList.add("建议参考教材第X章第Y节的内容进行复习");
        
        return adviceList;
    }
    
    @Override
    public Map<String, Object> searchExamsAndQuestions(Long studentId, String keywords) {
        if (studentId == null || !StringUtils.hasText(keywords)) {
            return new HashMap<>();
        }
        
        // 这里需要查询考试和问题信息
        // 实际实现可能需要联合查询多个表
        Map<String, Object> result = new HashMap<>();
        
        // 模拟返回结果
        List<Map<String, Object>> exams = new ArrayList<>();
        Map<String, Object> exam1 = new HashMap<>();
        exam1.put("examId", 1L);
        exam1.put("examTitle", "模拟考试1");
        exams.add(exam1);
        
        List<Map<String, Object>> questions = new ArrayList<>();
        Map<String, Object> question1 = new HashMap<>();
        question1.put("questionId", 1L);
        question1.put("content", "这是一道测试题目");
        questions.add(question1);
        
        result.put("exams", exams);
        result.put("questions", questions);
        
        return result;
    }
    
    /**
     * 将PO列表转换为BO列表
     * @param poList PO列表
     * @return BO列表
     */
    private List<StudentExamAnswerBO> convertToBOList(List<StudentExamAnswerPO> poList) {
        if (poList == null || poList.isEmpty()) {
            return new ArrayList<>();
        }
        
        return poList.stream()
                .map(StudentExamAnswerBO::fromPO)
                .collect(Collectors.toList());
    }
} 