package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nuist.bo.StudentExamAnswerBO;
import org.nuist.constant.QuestionType;
import org.nuist.mapper.ExamMapper;
import org.nuist.mapper.QuestionMapper;
import org.nuist.mapper.StudentExamMapper;
import org.nuist.po.ExamPo;
import org.nuist.po.QuestionPo;
import org.nuist.po.StudentExamAnswerPO;
import org.nuist.service.StudentExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学生考试服务实现类
 */
@Service
public class StudentExamServiceImpl implements StudentExamService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private StudentExamMapper studentExamMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<Long> getStudentIdsByExamId(Long examId) {
        if (examId == null) {
            return new ArrayList<>();
        }
        return studentExamMapper.getStudentIdsByExamAnswer(examId);
    }

    @Override
    public List<StudentExamAnswerBO> getStudentExamAnswers(Long studentId, Long examId) {
        if (studentId == null || examId == null) {
            return new ArrayList<>();
        }
        
        QueryWrapper<StudentExamAnswerPO> queryWrapper = Wrappers.<StudentExamAnswerPO>query()
                .eq("student_id", studentId)
                .eq("exam_id", examId);
        
        List<StudentExamAnswerPO> poList = studentExamMapper.selectList(queryWrapper);
        return convertToBOList(poList);
    }

    @Override
    public Boolean getIfExamAnsweredByStudent(Long studentId, Long examId) {
        Long studentExamCount = studentExamMapper.selectCount(
                Wrappers.<StudentExamAnswerPO>lambdaQuery()
                        .eq(StudentExamAnswerPO::getExamId, examId)
                        .eq(StudentExamAnswerPO::getStudentId, studentId)
        );
        return studentExamCount > 0;
    }

    @Override
    public List<Boolean> batchGetIfExamsAnsweredByStudent(Long studentId, List<Long> examIds) {
        return examIds.stream().map(examId -> {
            Long studentExamCount = studentExamMapper.selectCount(
                    Wrappers.<StudentExamAnswerPO>lambdaQuery()
                        .eq(StudentExamAnswerPO::getStudentId, studentId)
                        .eq(StudentExamAnswerPO::getExamId, examId)
            );
            return studentExamCount > 0;
        }).toList();
    }
    
    @Override
    public List<StudentExamAnswerBO> getStudentExamAnswersByTitle(Long studentId, String examTitle) {
        if (studentId == null || !StringUtils.hasText(examTitle)) {
            return new ArrayList<>();
        }
        
        // 先查询匹配title的考试ID
        List<Long> examIds = examMapper.selectList(Wrappers.<ExamPo>query()
                .select("exam_id")
                .like("title", "%" + examTitle + "%"))
                .stream()
                .map(ExamPo::getExamId)
                .collect(Collectors.toList());
        
        if (examIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 根据考试ID查询学生答案
        QueryWrapper<StudentExamAnswerPO> queryWrapper = Wrappers.<StudentExamAnswerPO>query()
                .eq("student_id", studentId)
                .in("exam_id", examIds);
                
        List<StudentExamAnswerPO> poList = studentExamMapper.selectList(queryWrapper);
        return convertToBOList(poList);
    }
    
    @Override
    public StudentExamAnswerBO getStudentQuestionAnswer(Long studentId, Long examId, Long questionId) {
        if (studentId == null || examId == null || questionId == null) {
            return null;
        }
        
        QueryWrapper<StudentExamAnswerPO> queryWrapper = Wrappers.<StudentExamAnswerPO>query()
                .eq("student_id", studentId)
                .eq("exam_id", examId)
                .eq("question_id", questionId);
                
        StudentExamAnswerPO po = studentExamMapper.selectOne(queryWrapper);
        return po != null ? StudentExamAnswerBO.fromPO(po) : null;
    }
    
    @Override
    public List<StudentExamAnswerBO> getStudentAnswersByQuestionContent(Long studentId, Long examId, String questionContent) {
        if (studentId == null || examId == null || !StringUtils.hasText(questionContent)) {
            return new ArrayList<>();
        }
        
        QueryWrapper<StudentExamAnswerPO> queryWrapper = Wrappers.<StudentExamAnswerPO>query()
                .eq("student_id", studentId)
                .eq("exam_id", examId)
                .like("question_content", "%" + questionContent + "%");
                
        List<StudentExamAnswerPO> poList = studentExamMapper.selectList(queryWrapper);
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
        if (po.getAnswerId() == null) {
            studentExamMapper.insert(po);
        } else {
            studentExamMapper.updateById(po);
        }
        
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
                    Long totalPoint = questionMapper.selectById(bo.getQuestionId()).getScorePoints();
                    if (bo.getCreatedAt() == null) {
                        bo.setCreatedAt(now);
                    }
                    bo.setUpdatedAt(now);
                    double rate = judgeQuestionIfCorrect(bo.getQuestionId(), bo.getStudentAnswer());
                    bo.setScore(rate * totalPoint);
                    return bo.toPO();
                })
                .collect(Collectors.toList());
        
        if (poList.isEmpty()) {
            return 0;
        }
        // 逐个插入或更新
        int count = 0;
        for (StudentExamAnswerPO po : poList) {
            if (po.getAnswerId() == null) {
                count += studentExamMapper.insert(po);
            } else {
                count += studentExamMapper.updateById(po);
            }
        }
        return count;
    }
    
    @Override
    public BigDecimal getExamScore(Long studentId, Long examId) {
        if (studentId == null || examId == null) {
            return BigDecimal.ZERO;
        }

        // 改用COALESCE函数（0
        QueryWrapper<StudentExamAnswerPO> queryWrapper = Wrappers.<StudentExamAnswerPO>query()
                .select("COALESCE(SUM(score)::numeric, 0) as total_score")
                .eq("student_id", studentId)
                .eq("exam_id", examId);

        Map<String, Object> result = studentExamMapper.selectMaps(queryWrapper).get(0);
        return parseTotalScore(result);
    }

    @Override
    public Map<Long, BigDecimal> getExamScoreByTitle(Long studentId, String examTitle) {
        if (studentId == null || !StringUtils.hasText(examTitle)) {
            return new HashMap<>();
        }
        
        // 先查询匹配title的考试ID
        List<Long> examIds = examMapper.selectList(Wrappers.<ExamPo>query()
                .select("exam_id")
                .like("title", "%" + examTitle + "%"))
                .stream()
                .map(ExamPo::getExamId)
                .collect(Collectors.toList());
        
        if (examIds.isEmpty()) {
            return new HashMap<>();
        }
        
        // 根据考试ID分组查询总分
        QueryWrapper<StudentExamAnswerPO> queryWrapper = Wrappers.<StudentExamAnswerPO>query()
                .select("exam_id", "IFNULL(SUM(score), 0) as total_score")
                .eq("student_id", studentId)
                .in("exam_id", examIds)
                .groupBy("exam_id");
                
        List<Map<String, Object>> results = studentExamMapper.selectMaps(queryWrapper);
        
        // 转换为Map<Long, BigDecimal>格式
        Map<Long, BigDecimal> scoreMap = new HashMap<>();
        for (Map<String, Object> result : results) {
            Long examId = Long.valueOf(result.get("exam_id").toString());
            BigDecimal totalScore = parseTotalScore(result);
            scoreMap.put(examId, totalScore);
        }
        
        return scoreMap;
    }
    
    @Override
    public List<Map<String, Object>> getStudentExamScores(Long studentId) {
        if (studentId == null) {
            return new ArrayList<>();
        }
        
//        QueryWrapper<StudentExamAnswerPO> queryWrapper = Wrappers.<StudentExamAnswerPO>query()
//                .select("exam_id", "exam_title", "SUM(score) as total_score", "COUNT(1) as question_count", "MAX(updated_at) as exam_time")
//                .eq("student_id", studentId)
//                .groupBy("exam_id", "exam_title");
                
        return studentExamMapper.getStudentExamScores(studentId);
    }
    
    @Override
    public List<Map<String, Object>> searchStudentExamScores(Long studentId, String titleKeywords) {
        if (studentId == null || !StringUtils.hasText(titleKeywords)) {
            return new ArrayList<>();
        }
        
//        QueryWrapper<StudentExamAnswerPO> queryWrapper = Wrappers.<StudentExamAnswerPO>query()
//                .select("exam_id", "exam_title", "SUM(score) as total_score", "COUNT(1) as question_count", "MAX(updated_at) as exam_time")
//                .eq("student_id", studentId)
//                .like("exam_title", "%" + titleKeywords + "%")
//                .groupBy("exam_id", "exam_title");
                
        return studentExamMapper.searchStudentExamScores(studentId, titleKeywords);
    }
    
    @Override
    public Map<String, Object> getExamDetail(Long studentId, Long examId) {
        if (studentId == null || examId == null) {
            return new HashMap<>();
        }
        
        // 查询考试总分
        BigDecimal totalScore = getExamScore(studentId, examId);
        
        // 查询考试详情
        QueryWrapper<StudentExamAnswerPO> queryWrapper = Wrappers.<StudentExamAnswerPO>query()
                .eq("student_id", studentId)
                .eq("exam_id", examId);
                
        List<Map<String, Object>> questionDetails = studentExamMapper.selectMaps(queryWrapper);
        
        // 组装结果
        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("examId", examId);
        result.put("totalScore", totalScore);
        result.put("questionCount", questionDetails.size());
        result.put("questions", questionDetails);
        
        return result;
    }
    
    @Override
    public Map<String, Object> getExamDetailByTitle(Long studentId, String examTitle) {
        if (studentId == null || !StringUtils.hasText(examTitle)) {
            return new HashMap<>();
        }
        
        // 查询考试ID
        List<Long> examIds = examMapper.selectList(Wrappers.<ExamPo>query()
                .select("exam_id")
                .like("title", "%" + examTitle + "%"))
                .stream()
                .map(ExamPo::getExamId)
                .collect(Collectors.toList());
        
        if (examIds.isEmpty()) {
            return new HashMap<>();
        }
        
        Long examId = examIds.get(0);
        return getExamDetail(studentId, examId);
    }
    
    @Override
    public Map<String, Object> analyzeExamResult(Long studentId, Long examId) {
        if (studentId == null || examId == null) {
            return new HashMap<>();
        }
        
        // 查询考试信息
        Map<String, Object> examDetail = getExamDetail(studentId, examId);
        
        // 分析知识点
        QueryWrapper<StudentExamAnswerPO> queryWrapper = Wrappers.<StudentExamAnswerPO>query()
                .select("knowledge_point", "AVG(score/max_score) as mastery_level", "COUNT(1) as question_count")
                .eq("student_id", studentId)
                .eq("exam_id", examId)
                .groupBy("knowledge_point");
                
        List<Map<String, Object>> knowledgePoints = studentExamMapper.selectMaps(queryWrapper);
        
        examDetail.put("knowledgePoints", knowledgePoints);
        
        return examDetail;
    }
    
    @Override
    public Map<String, Object> analyzeExamResultByTitle(Long studentId, String examTitle) {
        if (studentId == null || !StringUtils.hasText(examTitle)) {
            return new HashMap<>();
        }
        
        // 查询考试ID
        List<Long> examIds = examMapper.selectList(Wrappers.<ExamPo>query()
                .select("exam_id")
                .like("title", "%" + examTitle + "%"))
                .stream()
                .map(ExamPo::getExamId)
                .collect(Collectors.toList());
        
        if (examIds.isEmpty()) {
            return new HashMap<>();
        }
        
        Long examId = examIds.get(0);
        return analyzeExamResult(studentId, examId);
    }
    
    @Override
    public Map<String, Object> intelligentEvaluateAnswer(Long answerId) {
        if (answerId == null) {
            return new HashMap<>();
        }
        
        StudentExamAnswerPO po = studentExamMapper.selectById(answerId);
        if (po == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("answerId", po.getAnswerId());
        result.put("questionId", po.getQuestionId());
        result.put("studentAnswer", po.getStudentAnswer());
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
        
        // 查询考试ID
        List<Long> examIds = examMapper.selectList(Wrappers.<ExamPo>query()
                .select("exam_id")
                .like("title", "%" + examTitle + "%"))
                .stream()
                .map(ExamPo::getExamId)
                .collect(Collectors.toList());
        
        if (examIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 查找相关答案
        QueryWrapper<StudentExamAnswerPO> queryWrapper = Wrappers.<StudentExamAnswerPO>query()
                .eq("student_id", studentId)
                .in("exam_id", examIds)
                .like("question_content", "%" + questionContent + "%");
                
        List<StudentExamAnswerPO> answerList = studentExamMapper.selectList(queryWrapper);
        
        // 对每个答案进行智能评测
        return answerList.stream()
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
    }
    
    @Override
    public List<String> generateLearningAdvice(Long studentId, Long examId) {
        if (studentId == null || examId == null) {
            return new ArrayList<>();
        }
        
        // 分析考试结果
        Map<String, Object> analysis = analyzeExamResult(studentId, examId);
        
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
        
        // 查询考试ID
        List<Long> examIds = examMapper.selectList(Wrappers.<ExamPo>query()
                .select("exam_id")
                .like("title", "%" + examTitle + "%"))
                .stream()
                .map(ExamPo::getExamId)
                .collect(Collectors.toList());
        
        if (examIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        Long examId = examIds.get(0);
        // 分析考试结果
        Map<String, Object> analysis = analyzeExamResult(studentId, examId);
        
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
        
        // 查询匹配的考试
        List<Map<String, Object>> exams = examMapper.selectMaps(Wrappers.<ExamPo>query()
                .select("exam_id", "title as exam_title")
                .like("title", "%" + keywords + "%"));
        
        // 查询匹配的问题
        QueryWrapper<StudentExamAnswerPO> questionQueryWrapper = Wrappers.<StudentExamAnswerPO>query()
                .select("DISTINCT question_id, question_content")
                .eq("student_id", studentId)
                .like("question_content", "%" + keywords + "%");
                
        List<Map<String, Object>> questions = studentExamMapper.selectMaps(questionQueryWrapper);
        
        // 组装结果
        Map<String, Object> result = new HashMap<>();
        result.put("exams", exams);
        result.put("questions", questions);
        
        return result;
    }

    // TODO 简答和填空判题
    private double judgeQuestionIfCorrect(Long questionId, String studentAnswer) {
        QuestionPo questionPo = questionMapper.selectById(questionId);
        if(Objects.equals(questionPo.getQuestionType(), QuestionType.CHOICE.getCode())
            || Objects.equals(questionPo.getQuestionType(), QuestionType.JUDGMENT.getCode())){
            return 1;
        }
        else {
            return 0.5;
        }
    }
    
    /**
     * 从查询结果中解析总分
     * @param result 查询结果
     * @return 总分
     */
    private BigDecimal parseTotalScore(Map<String, Object> result) {
        if (result == null || result.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        Object totalScore = result.get("total_score");
        return totalScore != null ? new BigDecimal(totalScore.toString()) : BigDecimal.ZERO;
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