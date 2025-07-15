package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nuist.bo.ProblemBO;
import org.nuist.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "problem", description = "题目管理API")
@RequestMapping("/api/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemBO> getProblemById(@PathVariable Long problemId) {
        return ResponseEntity.ok(problemService.getProblemById(problemId));
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<ProblemBO>> getProblemsByAssignmentId(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(problemService.getProblemsByAssignmentId(assignmentId));
    }

    @GetMapping("/assignment/{assignmentId}/type/{type}")
    public ResponseEntity<List<ProblemBO>> getProblemsByAssignmentIdAndType(
            @PathVariable Long assignmentId,
            @PathVariable String type
    ) {
        return ResponseEntity.ok(problemService.getProblemsByAssignmentIdAndType(assignmentId, type));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ProblemBO>> getProblemsByType(@PathVariable String type) {
        return ResponseEntity.ok(problemService.getProblemsByType(type));
    }

    @PostMapping("/save")
    public ResponseEntity<ProblemBO> saveProblem(@RequestBody ProblemBO problemBO) {
        return ResponseEntity.ok(problemService.saveProblem(problemBO));
    }

    @PostMapping("/update")
    public ResponseEntity<ProblemBO> updateProblem(@RequestBody ProblemBO problemBO) {
        return ResponseEntity.ok(problemService.updateProblem(problemBO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HashMap<Object, Object>> deleteProblem(@RequestBody Long problemId) {
        boolean result = problemService.deleteProblem(problemId);
        return ResponseEntity.ok(new HashMap<>(){{
            put("success", result);
            put("message", result ? "题目删除成功" : "题目删除失败");
        }});
    }


}