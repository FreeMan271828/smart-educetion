package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.ExamBO;
import org.nuist.dto.AddExamDTO;
import org.nuist.dto.UpdateExamDTO;
import org.nuist.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "exam", description = "考试相关API")
@RequestMapping("/api/exam")
@RequiredArgsConstructor
public class ExamController implements org.nuist.client.ExamClient {

    private final ExamService examService;

    @GetMapping("/{examId}")
    @Override
    public ResponseEntity<ExamBO> getExamById(@PathVariable Long examId) {
        return ResponseEntity.ok(examService.getExamById(examId));
    }

    @GetMapping("/course/{courseId}")
    @Override
    public ResponseEntity<List<ExamBO>> getExamsInCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(examService.getExamsByCourseId(courseId));
    }

    @GetMapping("/teacher/{teacherId}")
    @Override
    public ResponseEntity<List<ExamBO>> getExamsByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(examService.getExamsByTeacherId(teacherId));
    }

    @GetMapping("/course/{courseId}/teacher/{teacherId}")
    @Override
    public ResponseEntity<List<ExamBO>> getExamsInCourseByTeacher(
            @PathVariable Long courseId, @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(examService.getExamsByTeacherInCourse(courseId, teacherId));
    }

    @PostMapping("/save")
    @Override
    public ResponseEntity<ExamBO> saveExam(@RequestBody AddExamDTO addExamDTO) {
        return ResponseEntity.ok(examService.saveExam(addExamDTO));
    }

    @PutMapping("/update")
    @Override
    public ResponseEntity<ExamBO> updateExam(@RequestBody UpdateExamDTO updateExamDTO) {
        return ResponseEntity.ok(examService.updateExam(updateExamDTO));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Map<String, Object>> deleteExam(@PathVariable Long id) {
        boolean result = examService.deleteExam(id);
        return ResponseEntity.ok(new HashMap<>(){{
            put("success", result);
            put("message", result ? "考试删除成功" : "考试删除失败");
        }});
    }
}
