package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.nuist.bo.KnowledgeBO;
import org.nuist.dto.ResortKnowledgeDTO;
import org.nuist.service.KnowledgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
public class KnowledgeController {

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

    @Operation(summary = "根据关键词，搜索标题/描述文本匹配的知识点条目")
    @GetMapping("/search")
    public ResponseEntity<List<KnowledgeBO>> searchKnowledge(@RequestParam String keyword) {
        return ResponseEntity.ok(knowledgeService.searchKnowledge(keyword));
    }

    @Operation(summary = "持久化一个知识点", description = "仅保存知识点信息，此操作不会将其与任何课程建立关联")
    @PostMapping("/save")
    public ResponseEntity<KnowledgeBO> saveKnowledge(@RequestBody AddKnowledgeDTO addKnowledgeDTO) {
        return ResponseEntity.ok(knowledgeService.saveKnowledge(addKnowledgeDTO));
    }

    @Operation(summary = "添加已有知识点到课程", description = "该操作会直接复用已有的知识点，使得多个课程引用同一个知识点对象。该操作不会检验传入ID的正确性")
    @PostMapping("/{knowledgeId}/append/course/{courseId}")
    public ResponseEntity<Map<String, Object>> appendKnowledgeToCourse(@PathVariable Long knowledgeId, @PathVariable Long courseId) {
        boolean success = knowledgeService.appendKnowledgeToCourse(courseId, knowledgeId);
        return ResponseEntity.ok(new HashMap<>(){{
            put("success", success);
            put("message", success ? "知识点复用添加到课程成功" : "操作失败，请检查参数合法性");
        }});
    }

    @Operation(summary = "复制并添加已有知识点到课程", description = "复制后的知识点为全新实体，仅与原知识点内容相同。该操作会检验传入knowledgeId的正确性")
    @PostMapping("/{knowledgeId}/copy/course/{courseId}")
    public ResponseEntity<KnowledgeBO> copyKnowledgeToCourse(@PathVariable Long knowledgeId, @PathVariable Long courseId) {
        return ResponseEntity.ok(knowledgeService.copyKnowledgeToCourse(courseId, knowledgeId));
    }

    @PutMapping("/update")
    public ResponseEntity<KnowledgeBO> updateKnowledge(@RequestBody UpdateKnowledgeDTO updateKnowledgeDTO) {
        return ResponseEntity.ok(knowledgeService.updateKnowledge(updateKnowledgeDTO));
    }

    @Operation(summary = "调整课程中单个知识点的位置", description = "将指定课程中的指定知识点移动到指定位置（位置计数从1开始）")
    @PutMapping("/resort-knowledge")
    public ResponseEntity<Map<String, Object>> resortKnowledgeInCourse(@RequestBody ResortKnowledgeDTO resortKnowledgeDTO) {
        boolean result = knowledgeService.resortKnowledge(
                resortKnowledgeDTO.getKnowledgeId(),
                resortKnowledgeDTO.getCourseId(),
                resortKnowledgeDTO.getPosition()
        );
        return ResponseEntity.ok(new HashMap<>(){{
            put("success", result);
            put("message", result ? "重排序完成" : "重排序失败");
        }});
    }

    @Operation(summary = "从课程中移除一条知识点", description = "此操作仅会移除关联关系，不会删除知识点实体")
    @DeleteMapping("/course/{courseId}/knowledge/{id}")
    public ResponseEntity<Map<String, Object>> deleteKnowledgeInCourse(@PathVariable Long courseId, @PathVariable Long id) {
        boolean result = knowledgeService.deleteKnowledgeInCourse(id, courseId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", result);
        resp.put("message", result ? "知识点移除成功" : "知识点移除失败");
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "从课程中移除一批知识点", description = "此操作仅会移除关联关系，不会删除知识点实体")
    @DeleteMapping("/course/{courseId}/batch")
    public ResponseEntity<Map<String, Object>> deleteKnowledgeInCourseBatch(
            @PathVariable Long courseId,
            @RequestBody List<Long> ids
    ) {
        boolean result = knowledgeService.batchDeleteKnowledgeInCourse(ids, courseId);
        return ResponseEntity.ok(new HashMap<>(){{
            put("success", result);
            put("message", result ? "批量移除知识点成功" : "批量移除知识点失败");
        }});
    }

    @Operation(summary = "移除知识点持久化")
    @DeleteMapping("/{knowledgeId}")
    public ResponseEntity<Map<String, Object>> deleteKnowledgeById(@PathVariable Long knowledgeId) {
        boolean success = knowledgeService.deleteKnowledge(knowledgeId);
        return ResponseEntity.ok(new HashMap<>(){{
            put("success", success);
            put("message", success ? "删除知识点成功" : "删除知识点失败");
        }});
    }

    @Operation(summary = "批量移除知识点持久化")
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> deleteKnowledgeBatch(@RequestBody List<Long> ids) {
        boolean success = knowledgeService.batchDeleteKnowledge(ids);
        return ResponseEntity.ok(new HashMap<>(){{
            put("success", success);
            put("message", success ? "批量删除知识点成功" : "批量删除知识点失败");
        }});
    }
}
