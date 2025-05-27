package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.QuestionBO;
import org.nuist.dto.AddQuestionDTO;
import org.nuist.dto.UpdateQuestionDTO;
import org.nuist.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "question", description = "课后问题相关API")
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController implements org.nuist.client.QuestionClient {

    private final QuestionService questionService;

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<QuestionBO> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionByID(id));
    }

    @GetMapping("/knowledge/{knowledgeId}")
    @Override
    public ResponseEntity<List<QuestionBO>> getQuestionByKnowledgeId(@PathVariable Long knowledgeId) {
        return ResponseEntity.ok(questionService.getQuestionsInKnowledge(knowledgeId));
    }

    @GetMapping("/teacher/{teacherId}")
    @Override
    public ResponseEntity<List<QuestionBO>> getQuestionByTeacherId(@PathVariable Long teacherId) {
        return ResponseEntity.ok(questionService.getQuestionsByTeacherId(teacherId));
    }

    @GetMapping("/knowledge/{knowledgeId}/search/content")
    @Override
    public ResponseEntity<List<QuestionBO>> searchQuestionInKnowledge(
            @PathVariable Long knowledgeId,
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(questionService.searchQuestionsByKeyword(knowledgeId, keyword));
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
    public ResponseEntity<QuestionBO> saveQuestion(@RequestBody AddQuestionDTO addQuestionDTO) {
        return ResponseEntity.ok(questionService.saveQuestion(addQuestionDTO));
    }

    @PutMapping("/update")
    @Override
    public ResponseEntity<QuestionBO> updateQuestion(@RequestBody UpdateQuestionDTO updateQuestionDTO) {
        return ResponseEntity.ok(questionService.updateQuestion(updateQuestionDTO));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Map<String, Object>> removeQuestion(@PathVariable Long id) {
        boolean result = questionService.deleteQuestion(id);
        return ResponseEntity.ok(new HashMap<>(){{
            put("success", result);
            put("message", result ? "课程删除成功" : "课程删除失败");
        }});
    }
}
