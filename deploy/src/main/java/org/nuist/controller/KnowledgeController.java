package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.nuist.bo.KnowledgeBO;
import org.nuist.service.KnowledgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.nuist.dto.AddKnowledgeDTO;
import org.nuist.dto.UpdateKnowledgeDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "knowledge", description = "知识点相关API")
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class KnowledgeController  {

    private final KnowledgeService knowledgeService;

    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeBO> getKnowledgeById(@PathVariable Long id) {
        return ResponseEntity.ok(knowledgeService.getKnowledgeById(id));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<KnowledgeBO>> getKnowledgeByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(knowledgeService.getKnowledgeByCourseId(courseId));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<KnowledgeBO>> getKnowledgeByTeacherId(@PathVariable Long teacherId) {
        return ResponseEntity.ok(knowledgeService.getKnowledgeByTeacherId(teacherId));
    }

    @GetMapping("/course/{courseId}/teacher/{teacherId}")
    public ResponseEntity<List<KnowledgeBO>> getKnowledgeByTeacherInCourse(@PathVariable Long courseId,
            @PathVariable Long teacherId) {
        return ResponseEntity.ok(knowledgeService.getKnowledgeByTeacherInCourse(courseId, teacherId));
    }

    @PostMapping("/save")
    public ResponseEntity<KnowledgeBO> saveKnowledge(@RequestBody AddKnowledgeDTO addKnowledgeDTO) {
        return ResponseEntity.ok(knowledgeService.saveKnowledge(addKnowledgeDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<KnowledgeBO> updateKnowledge(@RequestBody UpdateKnowledgeDTO updateKnowledgeDTO) {
        return ResponseEntity.ok(knowledgeService.updateKnowledge(updateKnowledgeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteKnowledge(@PathVariable Long id) {
        boolean result = knowledgeService.deleteKnowledge(id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", result);
        resp.put("message", result ? "知识点删除成功" : "知识点删除失败");
        return ResponseEntity.ok(resp);
    }
}
