package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nuist.business_object.TeacherBO;
import org.nuist.dto.AddTeacherDTO;
import org.nuist.dto.UpdateTeacherDTO;
import org.nuist.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "teacher", description = "教师相关API")
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/{id}")
    public ResponseEntity<TeacherBO> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<TeacherBO> getTeacherByUsername(@PathVariable String username) {
        return ResponseEntity.ok(teacherService.getTeacherByUsername(username));
    }

    @PostMapping("/save")
    public ResponseEntity<TeacherBO> saveTeacher(@RequestBody AddTeacherDTO addTeacherDTO) {
        return ResponseEntity.ok(teacherService.saveTeacher(addTeacherDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<TeacherBO> updateTeacher(@RequestBody UpdateTeacherDTO updateTeacherDTO) {
        return ResponseEntity.ok(teacherService.updateTeacher(updateTeacherDTO));
    }
}