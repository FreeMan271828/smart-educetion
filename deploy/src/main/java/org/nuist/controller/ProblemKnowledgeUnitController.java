package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nuist.bo.ProblemKnowledgeUnitBO;
import org.nuist.service.ProblemKnowledgeUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/problemKnowledgeUnit")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "problemKnowledgeUnit", description = "题目库和知识单元关系API")
public class ProblemKnowledgeUnitController {
    @Autowired
    private ProblemKnowledgeUnitService problemKnowledgeUnitService;






    @Operation(summary = "根据problemId获取所有知识单元")
    @GetMapping("/getKnowledgeUnitIdByProblemId/{problemId}")
    public Map<String, Object> getAllKnowledgeUnitIdByProblemId(@PathVariable Long problemId) {
        return problemKnowledgeUnitService.getAllKnowledgeUnitIdByProblemId(problemId);
    }

    @Operation(summary = "根据knowledgeUnitId获取所有题库中题目")
    @GetMapping("/getProblemIdByKnowledgeUnitId/{knowledgeUnitId}")
    public Map<String, Object> getAllProblemIdByKnowledgeUnitId(@PathVariable Long knowledgeUnitId) {
        return problemKnowledgeUnitService.getProblemIdByKnowledgeUnitId(knowledgeUnitId);
    }

    @Operation(summary = "添加题目和知识单元关系")
    @PostMapping("/add")
    public Map<String, Object> addProblemKnowledgeUnit(@RequestBody ProblemKnowledgeUnitBO problemKnowledgeUnitBO){
        return problemKnowledgeUnitService.addProblemKnowledgeUnit(problemKnowledgeUnitBO);
    }

    @Operation(summary = "批量添加题目和知识单元关系")
    @PostMapping("/batchAdd")
    public Map<String, Object> batchAddProblemKnowledgeUnit(@RequestBody List<ProblemKnowledgeUnitBO> problemKnowledgeUnitBOList){
        return problemKnowledgeUnitService.batchAddProblemKnowledgeUnits(problemKnowledgeUnitBOList);
    }

    @Operation(summary = "删除题目和知识单元关系")
    @DeleteMapping("/delete/{problemId}/{knowledgeUnitId}")
    public Map<String, Object> deleteProblemKnowledgeUnit(@PathVariable Long problemId, @PathVariable Long knowledgeUnitId){
        return problemKnowledgeUnitService.deleteProblemKnowledgeUnit(problemId, knowledgeUnitId);
    }

    @Operation(summary = "删除该题目的所有题目和知识单元关系")
    @DeleteMapping("/deleteByProblemId/{problemId}")
    public Map<String, Object> deleteProblemKnowledgeUnitByProblemId(@PathVariable Long problemId){
        return problemKnowledgeUnitService.deleteProblemKnowledgeUnit(problemId, null);
    }

    @Operation(summary = "删除该知识单元的所有题目和知识单元关系")
    @DeleteMapping("/deleteByKnowledgeUnitId/{knowledgeUnitId}")
    public Map<String, Object> deleteProblemKnowledgeUnitByKnowledgeUnitId(@PathVariable Long knowledgeUnitId){
        return problemKnowledgeUnitService.deleteProblemKnowledgeUnit(null, knowledgeUnitId);
    }

}
