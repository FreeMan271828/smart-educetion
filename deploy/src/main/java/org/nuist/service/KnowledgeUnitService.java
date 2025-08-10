package org.nuist.service;

import org.nuist.bo.KnowledgeUnitBO;

import java.util.List;
import java.util.Map;

public interface KnowledgeUnitService {
    Map<String, Object> analyzeTargetKnowledge(String target, String subject);

    Map<String, Object> addKnowledgeUnit(KnowledgeUnitBO knowledgeUnitBO);

    KnowledgeUnitBO getKnowledgeUnitById(Long id);

    Map<String, Object> batchAddKnowledgeUnits(List<KnowledgeUnitBO> knowledgeUnitBOList);

    Map<String, Object> batchDelete(List<Long> ids);

    // 通用分页查询方法（按状态过滤）

    Map<String, Object> getByStatusPage(Integer status, Integer current, Integer size);

    // 通用分页查询方法（按学科+状态过滤）
    Map<String, Object> getBySubjectAndStatusPage(String subject, Integer status, Integer current, Integer size);

    List<KnowledgeUnitBO> getAllKnowledgeUnitsBySubject(String subject);

    Map<String, Object> batchUpdateStatus(Integer status, List<Long> ids);

    List<KnowledgeUnitBO> searchKnowledgeUnit(String keyword);
}
