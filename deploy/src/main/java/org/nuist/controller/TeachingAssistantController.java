package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nuist.dto.LessonImproveDTO;
import org.nuist.dto.LessonRequestDTO;
import org.nuist.service.TeachingAssistantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RestController
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "teachingAssistant", description = "教师智能备课API")
@RequestMapping("/api/teaching-assistant")
@RequiredArgsConstructor
public class TeachingAssistantController {

    private final TeachingAssistantService teachingAssistantService;

    @PostMapping("/lesson/generate")
    @Operation(summary = "生成教学方案", description = "subjectType: 学科类型/课程名称；courseOutline: 知识点名称/知识点内容大纲")
    public ResponseEntity<Map<String, Object>> generateTeachingPlan(@RequestBody LessonRequestDTO lessonRequestDTO) {
        return ResponseEntity.ok(teachingAssistantService.generateTeachingPlan(
                lessonRequestDTO.getSubjectType(),
                lessonRequestDTO.getCourseOutline(),
                lessonRequestDTO.getDuration(),
                lessonRequestDTO.getDifficultyLevel(),
                lessonRequestDTO.getTeachingStyle()
        ));
    }

    @PostMapping("/analytics/course/{courseId}/student/{studentId}")
    @Operation(summary = "分析学生课程学习情况")
    public ResponseEntity<Map<String, Object>> analyzeCourseLearning(
            @PathVariable Long courseId,
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(teachingAssistantService.analyzeCourseLearning(courseId, studentId));
    }

    @PostMapping("/lesson/improve")
    @Operation(summary = "在已有教学方案的基础上进行改进", description = "一般场景为：AI生成教案之后，用户自行输入改进建议，重新生成")
    public ResponseEntity<Map<String, Object>> improveLesson(@RequestBody LessonImproveDTO dto) {
        return ResponseEntity.ok(teachingAssistantService.improveTeachingPlan(
                dto.getTeachingPlan(), dto.getSuggestion()
        ));
    }
}
