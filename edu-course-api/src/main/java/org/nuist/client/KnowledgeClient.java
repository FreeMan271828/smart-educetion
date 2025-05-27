package org.nuist.client;

import org.nuist.bo.KnowledgeBO;
import org.nuist.dto.AddKnowledgeDTO;
import org.nuist.dto.UpdateKnowledgeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "knowledgeService")
public interface KnowledgeClient {
    /**
     * 根据知识点主键查询一个知识点
     * @param id 知识点ID
     * @return 知识点
     */
    @GetMapping("/api/knowledge/{id}")
    ResponseEntity<KnowledgeBO> getKnowledgeById(@PathVariable Long id);

    @GetMapping("/api/knowledge/course/{courseId}")
    ResponseEntity<List<KnowledgeBO>> getKnowledgeByCourseId(@PathVariable Long courseId);

    @GetMapping("/api/knowledge/teacher/{teacherId}")
    ResponseEntity<List<KnowledgeBO>> getKnowledgeByTeacherId(@PathVariable Long teacherId);

    @GetMapping("/api/knowledge/course/{courseId}/teacher/{teacherId}")
    ResponseEntity<List<KnowledgeBO>> getKnowledgeByTeacherInCourse(@PathVariable Long courseId,
                                                                    @PathVariable Long teacherId);

    @PostMapping("/api/knowledge/save")
    ResponseEntity<KnowledgeBO> saveKnowledge(@RequestBody AddKnowledgeDTO addKnowledgeDTO);

    @PutMapping("/api/knowledge/update")
    ResponseEntity<KnowledgeBO> updateKnowledge(@RequestBody UpdateKnowledgeDTO updateKnowledgeDTO);

    @DeleteMapping("/api/knowledge/{id}")
    ResponseEntity<Map<String, Object>> deleteKnowledge(@PathVariable Long id);
}
