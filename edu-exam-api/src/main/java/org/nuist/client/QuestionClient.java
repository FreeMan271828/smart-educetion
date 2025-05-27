package org.nuist.client;

import org.nuist.bo.QuestionBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@FeignClient(name = "questionService")
public interface QuestionClient {
    @GetMapping("/api/question/{questionId}")
    ResponseEntity<QuestionBO> getQuestionById(@PathVariable Long questionId);

    @GetMapping("/api/question/teacher/{teacherId}")
    ResponseEntity<List<QuestionBO>> getQuestionsByTeacher(@PathVariable Long teacherId);

    @GetMapping("/api/question/type/{questionType}")
    ResponseEntity<List<QuestionBO>> getQuestionsByType(@PathVariable String questionType);

    @GetMapping("/api/question/difficulty/{difficulty}")
    ResponseEntity<List<QuestionBO>> getQuestionsByDifficulty(@PathVariable String difficulty);

    @GetMapping("/api/question/knowledge/{knowledgeId}")
    ResponseEntity<List<QuestionBO>> getQuestionsByKnowledge(@PathVariable Long knowledgeId);

    @GetMapping("/api/question/knowledge/{knowledgeId}/conditions")
    ResponseEntity<List<QuestionBO>> searchQuestionsInKnowledgeConditionally(
            @PathVariable Long knowledgeId,
            @RequestParam(required = false) String questionType,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) LocalDate startTime,
            @RequestParam(required = false) LocalDate endTime
    );

    @PostMapping("/api/question/save")
    ResponseEntity<QuestionBO> saveQuestion(@RequestBody QuestionBO questionBO);

    @PutMapping("/api/question/update")
    ResponseEntity<QuestionBO> updateQuestion(@RequestBody QuestionBO questionBO);

    @DeleteMapping("/api/question/{id}")
    ResponseEntity<Map<String, Object>> deleteQuestion(@PathVariable Long id);
}