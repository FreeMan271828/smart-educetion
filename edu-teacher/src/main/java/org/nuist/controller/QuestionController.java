package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.QuestionBO;
import org.nuist.dto.AddQuestionDTO;
import org.nuist.dto.QuestionQueryDTO;
import org.nuist.dto.UpdateQuestionDTO;
import org.nuist.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "question", description = "课后问题相关API")
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/{id}")
    public ResponseEntity<QuestionBO> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionByID(id));
    }

    @GetMapping("/knowledge/{knowledgeId}")
    public ResponseEntity<List<QuestionBO>> getQuestionByKnowledgeId(@PathVariable Long knowledgeId) {
        return ResponseEntity.ok(questionService.getQuestionsInKnowledge(knowledgeId));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<QuestionBO>> getQuestionByTeacherId(@PathVariable Long teacherId) {
        return ResponseEntity.ok(questionService.getQuestionsByTeacherId(teacherId));
    }

    @GetMapping("/knowledge/{knowledgeId}/search/content")
    public ResponseEntity<List<QuestionBO>> searchQuestionInKnowledge(
            @PathVariable Long knowledgeId,
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(questionService.searchQuestionsByKeyword(knowledgeId, keyword));
    }

    @GetMapping("/knowledge/{knowledgeId}/conditions")
    public ResponseEntity<List<QuestionBO>> searchQuestionsInKnowledgeConditionally(
            @PathVariable Long knowledgeId,
            @RequestBody QuestionQueryDTO questionQueryDTO
    ) {
        return ResponseEntity.ok(questionService.getQuestionsByConditionInKnowledge(knowledgeId, questionQueryDTO));
    }

    @PostMapping("/save")
    public ResponseEntity<QuestionBO> saveQuestion(@RequestBody AddQuestionDTO addQuestionDTO) {
        return ResponseEntity.ok(questionService.saveQuestion(addQuestionDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<QuestionBO> updateQuestion(@RequestBody UpdateQuestionDTO updateQuestionDTO) {
        return ResponseEntity.ok(questionService.updateQuestion(updateQuestionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> removeQuestion(@PathVariable Long id) {
        boolean result = questionService.deleteQuestion(id);
        return ResponseEntity.ok(new HashMap<>(){{
            put("success", result);
            put("message", result ? "课程删除成功" : "课程删除失败");
        }});
    }
}
