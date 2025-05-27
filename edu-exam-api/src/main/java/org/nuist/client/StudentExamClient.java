package org.nuist.client;

import org.nuist.bo.StudentExamAnswerBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "studentExamClient")
public interface StudentExamClient {
    @GetMapping("/student/{studentId}/exam/{examId}")
    ResponseEntity<List<StudentExamAnswerBO>> getStudentExamAnswers(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId);

    @GetMapping("/student/{studentId}/exam/title")
    ResponseEntity<List<StudentExamAnswerBO>> getStudentExamAnswersByTitle(
            @PathVariable("studentId") Long studentId,
            @RequestParam("title") String examTitle);

    @GetMapping("/student/{studentId}/exam/{examId}/question/{questionId}")
    ResponseEntity<StudentExamAnswerBO> getStudentQuestionAnswer(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId,
            @PathVariable("questionId") Long questionId);

    @GetMapping("/student/{studentId}/exam/{examId}/search")
    ResponseEntity<List<StudentExamAnswerBO>> getStudentAnswersByQuestionContent(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId,
            @RequestParam("content") String content);

    @PostMapping("/submit")
    ResponseEntity<Map<String, Object>> submitAnswer(@RequestBody StudentExamAnswerBO answer);

    @PostMapping("/batch-submit")
    ResponseEntity<Map<String, Object>> batchSubmitAnswers(@RequestBody List<StudentExamAnswerBO> answers);

    @GetMapping("/student/{studentId}/exam/{examId}/score")
    ResponseEntity<Map<String, Object>> getExamScore(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId);

    @GetMapping("/student/{studentId}/exam/title/score")
    ResponseEntity<Map<String, Object>> getExamScoreByTitle(
            @PathVariable("studentId") Long studentId,
            @RequestParam("title") String examTitle);

    @GetMapping("/student/{studentId}/scores")
    ResponseEntity<List<Map<String, Object>>> getStudentExamScores(
            @PathVariable("studentId") Long studentId);

    @GetMapping("/student/{studentId}/scores/search")
    ResponseEntity<List<Map<String, Object>>> searchStudentExamScores(
            @PathVariable("studentId") Long studentId,
            @RequestParam("keywords") String titleKeywords);

    @GetMapping("/student/{studentId}/exam/{examId}/detail")
    ResponseEntity<Map<String, Object>> getExamDetail(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId);

    @GetMapping("/student/{studentId}/exam/title/detail")
    ResponseEntity<Map<String, Object>> getExamDetailByTitle(
            @PathVariable("studentId") Long studentId,
            @RequestParam("title") String examTitle);

    @GetMapping("/student/{studentId}/exam/{examId}/analysis")
    ResponseEntity<Map<String, Object>> analyzeExamResult(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId);

    @GetMapping("/student/{studentId}/exam/title/analysis")
    ResponseEntity<Map<String, Object>> analyzeExamResultByTitle(
            @PathVariable("studentId") Long studentId,
            @RequestParam("title") String examTitle);

    @GetMapping("/answer/{answerId}/evaluate")
    ResponseEntity<Map<String, Object>> intelligentEvaluateAnswer(
            @PathVariable("answerId") Long answerId);

    @GetMapping("/student/{studentId}/evaluate-by-content")
    ResponseEntity<List<Map<String, Object>>> intelligentEvaluateAnswerByContent(
            @PathVariable("studentId") Long studentId,
            @RequestParam("title") String examTitle,
            @RequestParam("content") String content);

    @GetMapping("/student/{studentId}/exam/{examId}/advice")
    ResponseEntity<List<String>> generateLearningAdvice(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId);

    @GetMapping("/student/{studentId}/exam/title/advice")
    ResponseEntity<List<String>> generateLearningAdviceByTitle(
            @PathVariable("studentId") Long studentId,
            @RequestParam("title") String examTitle);

    @GetMapping("/student/{studentId}/search-exams")
    ResponseEntity<Map<String, Object>> searchExamsAndQuestions(
            @PathVariable("studentId") Long studentId,
            @RequestParam("keywords") String keywords);
}
