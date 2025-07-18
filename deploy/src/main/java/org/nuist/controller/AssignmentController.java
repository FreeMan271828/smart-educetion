package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nuist.bo.AssignmentBO;
import org.nuist.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "assignment", description = "作业相关API")
@RequestMapping("/api/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping("/{assignmentId}")
    public ResponseEntity<AssignmentBO> getAssignmentById(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(assignmentId));
    }


    @GetMapping("/course/{courseId}/type/{type}")
    public ResponseEntity<List<AssignmentBO>> getAssignmentsInCourseByType(@PathVariable Long courseId, @PathVariable String type) {
        return ResponseEntity.ok(assignmentService.getAssignmentsInCourseByType(courseId, type));
    }

    @GetMapping("/creatorId/{creatorId}")
    public ResponseEntity<List<AssignmentBO>> getAssignmentsByCreatorId(@PathVariable Long creatorId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsInCourseByCreatorId(creatorId));
    }

    @GetMapping("creatorId/{creatorId}/type/{type}")
    public ResponseEntity<List<AssignmentBO>> getAssignmentsByCreatorIdAndType(@PathVariable Long creatorId, @PathVariable String type) {
        return ResponseEntity.ok(assignmentService.getAssignmentsInCourseByCreatorIdAndType(creatorId, type));
    }

    @GetMapping("/course/{courseId}/creatorId/{creatorId}")
    public ResponseEntity<List<AssignmentBO>> getAssignmentsInCourseByCreatorId(@PathVariable Long courseId, @PathVariable Long creatorId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsInCourseByCreatorIdCourseId(courseId, creatorId));
    }

    @PostMapping("/save")
    public ResponseEntity<AssignmentBO> saveAssignment(@RequestBody AssignmentBO assignmentBO) {
        return ResponseEntity.ok(assignmentService.saveAssignment(assignmentBO));
    }

    @PostMapping("/update")
    public ResponseEntity<AssignmentBO> updateAssignment(@RequestBody AssignmentBO assignmentBO) {
        return ResponseEntity.ok(assignmentService.updateAssignment(assignmentBO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HashMap<Object, Object>> deleteAssignment(@RequestBody Long assignmentId) {
        boolean result = assignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.ok(new HashMap<>(){{
            put("success", result);
            put("message", result ? "作业删除成功" : "作业删除失败");
        }});
    }


    @Operation(summary = "根据学生id获取该学生已选课程中还未完成的作业")
    @GetMapping("/incomplete/student/{studentId}")
    public ResponseEntity<List<AssignmentBO>> getAssignmentsByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(assignmentService.getIncompleteAssignments(studentId));
    }



    @GetMapping("/incomplete-student/course/{courseId}/assignment/{assignmentId}")
    @Operation(summary = "获取课程下还未完成指定作业的学生Id")
    public ResponseEntity<List<Long>> getIncompleteStudentsByCourseIdAndAssignmentId(@PathVariable Long courseId, @PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentService.getIncompleteStudentsByCourseIdAndAssignmentId(courseId, assignmentId));
    }
}
