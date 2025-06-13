package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.QuestionBO;
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
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/{questionId}")

    public ResponseEntity<QuestionBO> getQuestionById(@PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.getQuestionById(questionId));
    }

    @GetMapping("/teacher/{teacherId}")

    public ResponseEntity<List<QuestionBO>> getQuestionsByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(questionService.getQuestionsByTeacherId(teacherId));
    }

    @GetMapping("/type/{questionType}")

    public ResponseEntity<List<QuestionBO>> getQuestionsByType(@PathVariable String questionType) {
        return ResponseEntity.ok(questionService.getQuestionsByType(questionType));
    }

    @GetMapping("/difficulty/{difficulty}")

    public ResponseEntity<List<QuestionBO>> getQuestionsByDifficulty(@PathVariable String difficulty) {
        return ResponseEntity.ok(questionService.getQuestionsByDifficulty(difficulty));
    }

    @GetMapping("/knowledge/{knowledgeId}")
    @Operation(summary = "查找属于一个知识点的课后习题", description = "实现为通过exam找出属于一个知识点的课后习题exam，并返回对应question")
    public ResponseEntity<List<QuestionBO>> getQuestionsByKnowledge(@PathVariable Long knowledgeId) {
        return ResponseEntity.ok(questionService.getQuestionsByKnowledgeId(knowledgeId));
    }

    @GetMapping("/knowledge/{knowledgeId}/conditions")

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

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<QuestionBO>> getQuestionsByExam(@PathVariable Long examId) {
        return ResponseEntity.ok(questionService.getQuestionsInExam(examId));
    }

    @PostMapping("/save")
    @Operation(summary = "保存一个问题", description = "目前，所有问题应当与考试关联（通过examId），考试再与知识点关联")
    public ResponseEntity<QuestionBO> saveQuestion(@RequestBody QuestionBO questionBO) {
        return ResponseEntity.ok(questionService.saveQuestion(questionBO));
    }

    @PutMapping("/update")
    
    public ResponseEntity<QuestionBO> updateQuestion(@RequestBody QuestionBO questionBO) {
        return ResponseEntity.ok(questionService.updateQuestion(questionBO));
    }

    @DeleteMapping("/{id}")
    
    public ResponseEntity<Map<String, Object>> deleteQuestion(@PathVariable Long id) {
        boolean result = questionService.deleteQuestion(id);
        return ResponseEntity.ok(new HashMap<>() {{
            put("success", result);
            put("message", result ? "题目删除成功" : "题目删除失败");
        }});
    }
}