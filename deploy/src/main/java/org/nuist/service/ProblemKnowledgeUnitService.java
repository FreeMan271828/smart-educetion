package org.nuist.service;

import org.nuist.bo.ProblemKnowledgeUnitBO;

import java.util.List;
import java.util.Map;

public interface ProblemKnowledgeUnitService {
    Map<String, Object> getAllKnowledgeUnitIdByProblemId(Long problemId);

    Map<String, Object> getProblemIdByKnowledgeUnitId(Long knowledgeUnitId);

    Map<String, Object> addProblemKnowledgeUnit(ProblemKnowledgeUnitBO problemKnowledgeUnitBO);

    Map<String, Object> deleteProblemKnowledgeUnit(Long problemId, Long knowledgeUnitId);


    Map<String, Object> batchAddProblemKnowledgeUnits(List<ProblemKnowledgeUnitBO> problemKnowledgeUnitBOList);
}
