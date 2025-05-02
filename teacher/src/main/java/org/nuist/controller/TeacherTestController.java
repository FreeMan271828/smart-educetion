package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nuist.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "__test__", description = "此API用于测试用户鉴权，稍后可以直接删除")
@SecurityRequirement(name = "BearerAuth")
public class TeacherTestController {

@PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/api/teacher/test/teacherResource")
    public ResponseEntity<String> testTeacherResource(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok("Hello Teacher, " + user.getUsername());
    }
}
