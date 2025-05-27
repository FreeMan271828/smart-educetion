package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.nuist.bo.StudentExamAnswerBO;
import org.nuist.client.StudentExamClient;
import org.nuist.service.StudentExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生考试答案控制器
 */
@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/student-exam")
public class StudentExamController implements StudentExamClient {
    
    @Autowired
    private StudentExamService studentExamService;
    
    /**
     * 获取学生考试答案
     * @param studentId 学生ID
     * @param examId 考试ID
     * @return 考试答案列表
     */
    @GetMapping("/student/{studentId}/exam/{examId}")
    @Override
    public ResponseEntity<List<StudentExamAnswerBO>> getStudentExamAnswers(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId) {
        List<StudentExamAnswerBO> answers = studentExamService.getStudentExamAnswers(studentId, examId);
        return ResponseEntity.ok(answers);
    }

    /**
     * 根据考试标题获取学生考试答案
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 考试答案列表
     */
    @GetMapping("/student/{studentId}/exam/title")
    @Override
    public ResponseEntity<List<StudentExamAnswerBO>> getStudentExamAnswersByTitle(
            @PathVariable("studentId") Long studentId,
            @RequestParam("title") String examTitle) {
        List<StudentExamAnswerBO> answers = studentExamService.getStudentExamAnswersByTitle(studentId, examTitle);
        return ResponseEntity.ok(answers);
    }

    /**
     * 获取学生对特定问题的答案
     * @param studentId 学生ID
     * @param examId 考试ID
     * @param questionId 问题ID
     * @return 答案
     */
    @GetMapping("/student/{studentId}/exam/{examId}/question/{questionId}")
    @Override
    public ResponseEntity<StudentExamAnswerBO> getStudentQuestionAnswer(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId,
            @PathVariable("questionId") Long questionId) {
        StudentExamAnswerBO answer = studentExamService.getStudentQuestionAnswer(studentId, examId, questionId);
        if (answer != null) {
            return ResponseEntity.ok(answer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据问题内容搜索学生答案
     * @param studentId 学生ID
     * @param examId 考试ID
     * @param content 问题内容
     * @return 答案列表
     */
    @GetMapping("/student/{studentId}/exam/{examId}/search")
    @Override
    public ResponseEntity<List<StudentExamAnswerBO>> getStudentAnswersByQuestionContent(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId,
            @RequestParam("content") String content) {
        List<StudentExamAnswerBO> answers = studentExamService.getStudentAnswersByQuestionContent(
                studentId, examId, content);
        return ResponseEntity.ok(answers);
    }

    /**
     * 提交答案
     * @param answer 答案
     * @return 提交结果
     */
    @PostMapping("/submit")
    @Override
    public ResponseEntity<Map<String, Object>> submitAnswer(@RequestBody StudentExamAnswerBO answer) {
        Long answerId = studentExamService.submitAnswer(answer);

        Map<String, Object> result = new HashMap<>();
        if (answerId != null) {
            result.put("success", true);
            result.put("answerId", answerId);
            result.put("message", "答案提交成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "答案提交失败");
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 批量提交答案
     * @param answers 答案列表
     * @return 提交结果
     */
    @PostMapping("/batch-submit")
    @Override
    public ResponseEntity<Map<String, Object>> batchSubmitAnswers(@RequestBody List<StudentExamAnswerBO> answers) {
        int count = studentExamService.batchSubmitAnswers(answers);

        Map<String, Object> result = new HashMap<>();
        result.put("success", count > 0);
        result.put("count", count);
        result.put("message", count > 0 ? "批量提交成功" : "批量提交失败");
        return ResponseEntity.ok(result);
    }

    /**
     * 获取考试得分
     * @param studentId 学生ID
     * @param examId 考试ID
     * @return 得分
     */
    @GetMapping("/student/{studentId}/exam/{examId}/score")
    @Override
    public ResponseEntity<Map<String, Object>> getExamScore(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId) {
        BigDecimal score = studentExamService.getExamScore(studentId, examId);

        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("examId", examId);
        result.put("score", score);
        return ResponseEntity.ok(result);
    }

    /**
     * 根据考试标题获取得分
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 得分
     */
    @GetMapping("/student/{studentId}/exam/title/score")
    @Override
    public ResponseEntity<Map<String, Object>> getExamScoreByTitle(
            @PathVariable("studentId") Long studentId,
            @RequestParam("title") String examTitle) {
        Map<Long, BigDecimal> scoreMap = studentExamService.getExamScoreByTitle(studentId, examTitle);
        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("examTitle", examTitle);
        result.put("scoreMap", scoreMap);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取学生所有考试的得分
     * @param studentId 学生ID
     * @return 考试得分列表
     */
    @GetMapping("/student/{studentId}/scores")
    @Override
    public ResponseEntity<List<Map<String, Object>>> getStudentExamScores(
            @PathVariable("studentId") Long studentId) {
        List<Map<String, Object>> scores = studentExamService.getStudentExamScores(studentId);
        return ResponseEntity.ok(scores);
    }

    /**
     * 搜索学生考试得分
     * @param studentId 学生ID
     * @param titleKeywords 标题关键词
     * @return 考试得分列表
     */
    @GetMapping("/student/{studentId}/scores/search")
    @Override
    public ResponseEntity<List<Map<String, Object>>> searchStudentExamScores(
            @PathVariable("studentId") Long studentId,
            @RequestParam("keywords") String titleKeywords) {
        List<Map<String, Object>> scores = studentExamService.searchStudentExamScores(studentId, titleKeywords);
        return ResponseEntity.ok(scores);
    }

    /**
     * 获取考试详情
     * @param studentId 学生ID
     * @param examId 考试ID
     * @return 考试详情
     */
    @GetMapping("/student/{studentId}/exam/{examId}/detail")
    @Override
    public ResponseEntity<Map<String, Object>> getExamDetail(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId) {
        Map<String, Object> detail = studentExamService.getExamDetail(studentId, examId);
        return ResponseEntity.ok(detail);
    }

    /**
     * 根据考试标题获取详情
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 考试详情
     */
    @GetMapping("/student/{studentId}/exam/title/detail")
    @Override
    public ResponseEntity<Map<String, Object>> getExamDetailByTitle(
            @PathVariable("studentId") Long studentId,
            @RequestParam("title") String examTitle) {
        Map<String, Object> detail = studentExamService.getExamDetailByTitle(studentId, examTitle);
        return ResponseEntity.ok(detail);
    }

    /**
     * 分析考试结果
     * @param studentId 学生ID
     * @param examId 考试ID
     * @return 分析结果
     */
    @GetMapping("/student/{studentId}/exam/{examId}/analysis")
    @Override
    public ResponseEntity<Map<String, Object>> analyzeExamResult(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId) {
        Map<String, Object> analysis = studentExamService.analyzeExamResult(studentId, examId);
        return ResponseEntity.ok(analysis);
    }

    /**
     * 根据考试标题分析结果
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 分析结果
     */
    @GetMapping("/student/{studentId}/exam/title/analysis")
    @Override
    public ResponseEntity<Map<String, Object>> analyzeExamResultByTitle(
            @PathVariable("studentId") Long studentId,
            @RequestParam("title") String examTitle) {
        Map<String, Object> analysis = studentExamService.analyzeExamResultByTitle(studentId, examTitle);
        return ResponseEntity.ok(analysis);
    }

    /**
     * 智能评估答案
     * @param answerId 答案ID
     * @return 评估结果
     */
    @GetMapping("/answer/{answerId}/evaluate")
    @Override
    public ResponseEntity<Map<String, Object>> intelligentEvaluateAnswer(
            @PathVariable("answerId") Long answerId) {
        Map<String, Object> evaluation = studentExamService.intelligentEvaluateAnswer(answerId);
        return ResponseEntity.ok(evaluation);
    }

    /**
     * 根据内容智能评估答案
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @param content 问题内容
     * @return 评估结果列表
     */
    @GetMapping("/student/{studentId}/evaluate-by-content")
    @Override
    public ResponseEntity<List<Map<String, Object>>> intelligentEvaluateAnswerByContent(
            @PathVariable("studentId") Long studentId,
            @RequestParam("title") String examTitle,
            @RequestParam("content") String content) {
        List<Map<String, Object>> evaluations = studentExamService.intelligentEvaluateAnswerByContent(
                studentId, examTitle, content);
        return ResponseEntity.ok(evaluations);
    }

    /**
     * 生成学习建议
     * @param studentId 学生ID
     * @param examId 考试ID
     * @return 学习建议
     */
    @GetMapping("/student/{studentId}/exam/{examId}/advice")
    @Override
    public ResponseEntity<List<String>> generateLearningAdvice(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId) {
        List<String> advice = studentExamService.generateLearningAdvice(studentId, examId);
        return ResponseEntity.ok(advice);
    }

    /**
     * 根据考试标题生成学习建议
     * @param studentId 学生ID
     * @param examTitle 考试标题
     * @return 学习建议
     */
    @GetMapping("/student/{studentId}/exam/title/advice")
    @Override
    public ResponseEntity<List<String>> generateLearningAdviceByTitle(
            @PathVariable("studentId") Long studentId,
            @RequestParam("title") String examTitle) {
        List<String> advice = studentExamService.generateLearningAdviceByTitle(studentId, examTitle);
        return ResponseEntity.ok(advice);
    }

    /**
     * 搜索考试和问题
     * @param studentId 学生ID
     * @param keywords 关键词
     * @return 搜索结果
     */
    @GetMapping("/student/{studentId}/search-exams")
    @Override
    public ResponseEntity<Map<String, Object>> searchExamsAndQuestions(
            @PathVariable("studentId") Long studentId,
            @RequestParam("keywords") String keywords) {
        Map<String, Object> results = studentExamService.searchExamsAndQuestions(studentId, keywords);
        return ResponseEntity.ok(results);
    }
}