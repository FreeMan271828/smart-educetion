package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nuist.dto.LessonRequestDTO;
import org.nuist.service.TeachingAssistantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "teachingAssistant", description = "教师智能备课API")
@RequestMapping("/api/teaching-assistant")
@RequiredArgsConstructor
public class TeachingAssistantController {

    private final TeachingAssistantService teachingAssistantService;

    @PostMapping("/lesson/generate")
    public ResponseEntity<Map<String, Object>> generateTeachingPlan(@RequestBody LessonRequestDTO lessonRequestDTO) {
        return ResponseEntity.ok(teachingAssistantService.generateTeachingPlan(
                lessonRequestDTO.getSubjectType(),
                lessonRequestDTO.getCourseOutline(),
                lessonRequestDTO.getCourseDocuments(),
                lessonRequestDTO.getDuration(),
                lessonRequestDTO.getDifficultyLevel(),
                lessonRequestDTO.getTeachingStyle()
        ));
    }
}
