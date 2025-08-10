package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.nuist.bo.KnowledgeUnitBO;
import org.nuist.service.KnowledgeUnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/knowledgeUnit")
public class KnowledgeUnitController {

    private final KnowledgeUnitService knowledgeUnitService;


    public KnowledgeUnitController(KnowledgeUnitService knowledgeUnitService) {
        this.knowledgeUnitService = knowledgeUnitService;
    }


    @Operation(summary = "根据目标分析出知识单元,并将新知识单元存入数据库（状态：ai待审）")
    @PostMapping("/analyse-knowledge")
    public ResponseEntity<Map<String, Object>> analyzeKnowledge(
            @RequestParam("target") String target,
            @RequestParam("subject") String subject
    ) {
        return ResponseEntity.ok(knowledgeUnitService.analyzeTargetKnowledge(target, subject));
    }


    @Operation(summary = "搜索知识单元")
    @GetMapping("/search")
    public ResponseEntity<List<KnowledgeUnitBO>> searchKnowledgeUnit(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(knowledgeUnitService.searchKnowledgeUnit(keyword));
    }


    /**
     * 添加知识单元
     * @param knowledgeUnitBO
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addKnowledgeUnit(@RequestBody KnowledgeUnitBO knowledgeUnitBO){
        return ResponseEntity.ok(knowledgeUnitService.addKnowledgeUnit(knowledgeUnitBO));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<KnowledgeUnitBO> getKnowledgeUnit(@PathVariable("id") Long id){
        return ResponseEntity.ok(knowledgeUnitService.getKnowledgeUnitById(id));
    }

    @Operation(summary = "根据subject名获取所有知识单元")
    @GetMapping("/getAll/subject/{subject}")
    public ResponseEntity<List<KnowledgeUnitBO>> getAllKnowledgeUnits(@PathVariable("subject") String subject){
        return ResponseEntity.ok(knowledgeUnitService.getAllKnowledgeUnitsBySubject(subject));
    }

    @Operation(summary = "批量添加知识单元")
    @PostMapping("/batchAdd")
    public ResponseEntity<Map<String, Object>> batchAddKnowledgeUnits(
            @RequestBody List<KnowledgeUnitBO> knowledgeUnitBOList) {
        return ResponseEntity.ok(knowledgeUnitService.batchAddKnowledgeUnits(knowledgeUnitBOList));
    }

    @Operation(summary = "批量更新知识单元状态")
    @PostMapping("/batchUpdateStatus/{status}")
    public ResponseEntity<Map<String, Object>> batchUpdateStatus(
            @PathVariable("status") Integer status, // 状态值通过路径参数接收
            @RequestBody List<Long> ids) { // ID列表通过请求体接收
        return ResponseEntity.ok(knowledgeUnitService.batchUpdateStatus(status, ids));
    }


    @DeleteMapping("/batchDelete")
    public ResponseEntity<Map<String, Object>> batchDelete(
            @RequestBody List<Long> ids) {
        return ResponseEntity.ok(knowledgeUnitService.batchDelete(ids));
    }

    // 1. 根据subject名分页获取所有预置知识单元
    @Operation(summary = "根据subject名分页获取所有预置知识单元")
    @GetMapping("/preset/{subject}")
    public ResponseEntity<Map<String, Object>> getPresetBySubject(
            @PathVariable String subject,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(knowledgeUnitService.getBySubjectAndStatusPage(subject, 0, current, size));
    }

    // 2. 分页获取所有待审知识单元
    @Operation(summary = "分页获取所有待审知识单元")
    @GetMapping("/pending")
    public ResponseEntity<Map<String, Object>> getAllPending(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(knowledgeUnitService.getByStatusPage(1, current, size));
    }

    // 3. 根据subject名分页获取该subject所有待审知识单元
    @Operation(summary = "根据subject名分页获取该subject所有待审知识单元")
    @GetMapping("/pending/{subject}")
    public ResponseEntity<Map<String, Object>> getPendingBySubject(
            @PathVariable String subject,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(knowledgeUnitService.getBySubjectAndStatusPage(subject, 1, current, size));
    }

    // 4. 分页获取所有废弃知识单元
    @Operation(summary = "分页获取所有废弃知识单元")
    @GetMapping("/deprecated")
    public ResponseEntity<Map<String, Object>> getAllDeprecated(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(knowledgeUnitService.getByStatusPage(2, current, size));
    }
}
