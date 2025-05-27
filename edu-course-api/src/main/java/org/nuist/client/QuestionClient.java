package org.nuist.client;

import org.nuist.bo.QuestionBO;
import org.nuist.dto.AddQuestionDTO;
import org.nuist.dto.UpdateQuestionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@FeignClient(name = "questionService")
public interface QuestionClient {
    @GetMapping("/api/question/{id}")
    ResponseEntity<QuestionBO> getQuestionById(@PathVariable Long id);

    @GetMapping("/api/question/knowledge/{knowledgeId}")
    ResponseEntity<List<QuestionBO>> getQuestionByKnowledgeId(@PathVariable Long knowledgeId);

    @GetMapping("/api/question/teacher/{teacherId}")
    ResponseEntity<List<QuestionBO>> getQuestionByTeacherId(@PathVariable Long teacherId);

    @GetMapping("/api/question/knowledge/{knowledgeId}/search/content")
    ResponseEntity<List<QuestionBO>> searchQuestionInKnowledge(
            @PathVariable Long knowledgeId,
            @RequestParam String keyword
    );

    @GetMapping("/api/question/knowledge/{knowledgeId}/conditions")
    ResponseEntity<List<QuestionBO>> searchQuestionsInKnowledgeConditionally(
            @PathVariable Long knowledgeId,
            @RequestParam(required = false) String questionType,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) LocalDate startTime,
            @RequestParam(required = false) LocalDate endTime
    );

    @PostMapping("/api/question/save")
    ResponseEntity<QuestionBO> saveQuestion(@RequestBody AddQuestionDTO addQuestionDTO);

    @PutMapping("/api/question/update")
    ResponseEntity<QuestionBO> updateQuestion(@RequestBody UpdateQuestionDTO updateQuestionDTO);

    @DeleteMapping("/api/question/{id}")
    ResponseEntity<Map<String, Object>> removeQuestion(@PathVariable Long id);
}
