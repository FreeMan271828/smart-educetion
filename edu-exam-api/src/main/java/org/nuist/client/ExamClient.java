package org.nuist.client;

import org.nuist.bo.ExamBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "examService")
public interface ExamClient {
    @GetMapping("/api/exam/{examId}")
    ResponseEntity<ExamBO> getExamById(@PathVariable Long examId);

    @GetMapping("/api/exam/course/{courseId}")
    ResponseEntity<List<ExamBO>> getExamsInCourse(@PathVariable Long courseId);

    @GetMapping("/api/exam/teacher/{teacherId}")
    ResponseEntity<List<ExamBO>> getExamsByTeacher(@PathVariable Long teacherId);

    @GetMapping("/api/exam/course/{courseId}/teacher/{teacherId}")
    ResponseEntity<List<ExamBO>> getExamsInCourseByTeacher(
            @PathVariable Long courseId, @PathVariable Long teacherId
    );

    @PostMapping("/api/exam/save")
    ResponseEntity<ExamBO> saveExam(@RequestBody ExamBO examBo);

    @PutMapping("/api/exam/update")
    ResponseEntity<ExamBO> updateExam(@RequestBody ExamBO examBo);

    @DeleteMapping("/api/exam/{id}")
    ResponseEntity<Map<String, Object>> deleteExam(@PathVariable Long id);
}
