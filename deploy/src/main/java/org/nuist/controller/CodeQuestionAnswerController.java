package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.CodeQuestionAnswerBO;
import org.nuist.dto.request.CodeQuestionSubmissionDTO;
import org.nuist.service.CodeQuestionAnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/code-question-answer")
@Tag(name = "code-question-answer", description = "编程题作答记录相关接口")
@SecurityRequirement(name = "BearerAuth")
@RequiredArgsConstructor
public class CodeQuestionAnswerController {

    private final CodeQuestionAnswerService codeQuestionAnswerService;

    @GetMapping("/{answerId}")
    public ResponseEntity<CodeQuestionAnswerBO> getAnswer(@PathVariable Long answerId) {
        return ResponseEntity.ok(codeQuestionAnswerService.getCQuestionAnswer(answerId));
    }

    @GetMapping("/code-question/{cqId}")
    @Operation(summary = "查看一道编程题的所有提交记录")
    public ResponseEntity<List<CodeQuestionAnswerBO>> getAnswersInCQuestion(@PathVariable Long cqId) {
        return ResponseEntity.ok(codeQuestionAnswerService.getAnswersInCQuestion(cqId));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "查看一个学生的所有题目所有提交记录")
    public ResponseEntity<List<CodeQuestionAnswerBO>> getAnswersByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(codeQuestionAnswerService.getAnswersByStudent(studentId));
    }

    @GetMapping("/code-question/{cqId}/student/{studentId}")
    @Operation(summary = "查看一道编程题中，一位学生的所有提交记录")
    public ResponseEntity<List<CodeQuestionAnswerBO>> getStudentAnswersInCQuestion(@PathVariable Long cqId, @PathVariable Long studentId) {
        return ResponseEntity.ok(codeQuestionAnswerService.getStudentAnswersInCQuestion(cqId, studentId));
    }

    @GetMapping("/is-accepted/code-question/{cqId}/student/{studentId}")
    @Operation(summary = "检测一位学生是否AC过一道编程题")
    public ResponseEntity<Boolean> isAccepted(@PathVariable Long cqId, @PathVariable Long studentId) {
        return ResponseEntity.ok(codeQuestionAnswerService.isAccepted(studentId, cqId));
    }

    @PostMapping("/submit")
    @Operation(summary = "学生提交编程题答案，并触发评测判题，返回完整结果")
    public ResponseEntity<CodeQuestionAnswerBO> submit(@RequestBody CodeQuestionSubmissionDTO dto) {
        return ResponseEntity.ok(codeQuestionAnswerService.submit(
                dto.getStudentId(), dto.getCodeQuestionId(), dto.getStudentCode(), dto.getLanguage()
        ));
    }
}
