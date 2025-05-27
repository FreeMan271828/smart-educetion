package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.QuestionBO;
import org.nuist.client.QuestionClient;
import org.nuist.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "question", description = "题目相关API")
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController implements QuestionClient {

    private final QuestionService questionService;

    @GetMapping("/{questionId}")
    @Override
    public ResponseEntity<QuestionBO> getQuestionById(@PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.getQuestionById(questionId));
    }

    @GetMapping("/teacher/{teacherId}")
    @Override
    public ResponseEntity<List<QuestionBO>> getQuestionsByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(questionService.getQuestionsByTeacherId(teacherId));
    }

    @GetMapping("/type/{questionType}")
    @Override
    public ResponseEntity<List<QuestionBO>> getQuestionsByType(@PathVariable String questionType) {
        return ResponseEntity.ok(questionService.getQuestionsByType(questionType));
    }

    @GetMapping("/difficulty/{difficulty}")
    @Override
    public ResponseEntity<List<QuestionBO>> getQuestionsByDifficulty(@PathVariable String difficulty) {
        return ResponseEntity.ok(questionService.getQuestionsByDifficulty(difficulty));
    }

    @GetMapping("/knowledge/{knowledgeId}")
    @Override
    public ResponseEntity<List<QuestionBO>> getQuestionsByKnowledge(@PathVariable Long knowledgeId) {
        return ResponseEntity.ok(questionService.getQuestionsByKnowledgeId(knowledgeId));
    }

    @GetMapping("/knowledge/{knowledgeId}/conditions")
    @Override
    public ResponseEntity<List<QuestionBO>> searchQuestionsInKnowledgeConditionally(
            @PathVariable Long knowledgeId,
            @RequestParam(required = false) String questionType,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) LocalDate startTime,
            @RequestParam(required = false) LocalDate endTime
    ) {
        return ResponseEntity.ok(questionService.getQuestionsByConditionInKnowledge(
                knowledgeId, questionType, difficulty, startTime, endTime)
        );
    }

    @PostMapping("/save")
    @Override
    public ResponseEntity<QuestionBO> saveQuestion(@RequestBody QuestionBO questionBO) {
        return ResponseEntity.ok(questionService.saveQuestion(questionBO));
    }

    @PutMapping("/update")
    @Override
    public ResponseEntity<QuestionBO> updateQuestion(@RequestBody QuestionBO questionBO) {
        return ResponseEntity.ok(questionService.updateQuestion(questionBO));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Map<String, Object>> deleteQuestion(@PathVariable Long id) {
        boolean result = questionService.deleteQuestion(id);
        return ResponseEntity.ok(new HashMap<>() {{
            put("success", result);
            put("message", result ? "题目删除成功" : "题目删除失败");
        }});
    }
}