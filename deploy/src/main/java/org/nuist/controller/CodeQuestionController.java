package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.CodeQuestionBO;
import org.nuist.service.CodeQuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/code-question")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "code-question", description = "编程题相关接口")
@RequiredArgsConstructor
public class CodeQuestionController {

    private final CodeQuestionService codeQuestionService;

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<CodeQuestionBO>> getCodeQuestionsInExam(@PathVariable("examId") Long examId) {
        return ResponseEntity.ok(codeQuestionService.getCQuestionsInExam(examId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CodeQuestionBO> getCodeQuestion(@PathVariable("id") Long id) {
        return ResponseEntity.ok(codeQuestionService.getCodeQuestionById(id));
    }

    @PostMapping("/save")
    @Operation(summary = "保存一道编程题", description = "sample i/o为展示的样例输入输出，case i/o为评测判题使用的测试用例")
    public ResponseEntity<CodeQuestionBO> saveCodeQuestion(@RequestBody CodeQuestionBO codeQuestionBO) {
        return ResponseEntity.ok(codeQuestionService.insertCodeQuestion(codeQuestionBO));
    }

    @PutMapping("/update")
    public ResponseEntity<CodeQuestionBO> updateCodeQuestion(@RequestBody CodeQuestionBO codeQuestionBO) {
        return ResponseEntity.ok(codeQuestionService.updateCodeQuestion(codeQuestionBO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCodeQuestion(@PathVariable("id") Long id) {
        boolean success = codeQuestionService.deleteCodeQuestionById(id);
        return ResponseEntity.ok(new HashMap<>() {{
            put("success", success);
            put("message", success ? "删除题目成功" : "删除题目失败");
        }});
    }
}
