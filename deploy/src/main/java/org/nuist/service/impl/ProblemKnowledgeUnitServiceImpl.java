package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nuist.bo.ProblemKnowledgeUnitBO;
import org.nuist.mapper.KnowledgeUnitMapper;
import org.nuist.mapper.ProblemKnowledgeUnitMapper;
import org.nuist.po.KnowledgeUnitPO;
import org.nuist.po.ProblemKnowledgeUnitPO;
import org.nuist.service.ProblemKnowledgeUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProblemKnowledgeUnitServiceImpl extends ServiceImpl<ProblemKnowledgeUnitMapper, ProblemKnowledgeUnitPO> implements ProblemKnowledgeUnitService {
    @Autowired
    private KnowledgeUnitMapper knowledgeUnitMapper;
    @Autowired
    private ProblemKnowledgeUnitMapper problemKnowledgeUnitMapper;

    @Override
    public Map<String, Object> getAllKnowledgeUnitIdByProblemId(Long problemId) {
        LambdaQueryWrapper<ProblemKnowledgeUnitPO> queryWrapper = Wrappers.<ProblemKnowledgeUnitPO>lambdaQuery();
        queryWrapper.eq(ProblemKnowledgeUnitPO::getProblemId, problemId);
        List<ProblemKnowledgeUnitPO> problemKnowledgeUnitPOList = baseMapper.selectList(queryWrapper);
        List<Long> knowledgeUnitIds = problemKnowledgeUnitPOList.stream().map(ProblemKnowledgeUnitPO::getKnowledgeUnitId).toList();
        return Map.of("knowledgeUnitIds", knowledgeUnitIds);

    }

    @Override
    public Map<String, Object> getProblemIdByKnowledgeUnitId(Long knowledgeUnitId) {

        LambdaQueryWrapper<ProblemKnowledgeUnitPO> queryWrapper = Wrappers.<ProblemKnowledgeUnitPO>lambdaQuery();
        queryWrapper.eq(ProblemKnowledgeUnitPO::getKnowledgeUnitId, knowledgeUnitId);
        List<ProblemKnowledgeUnitPO> problemKnowledgeUnitPOList = baseMapper.selectList(queryWrapper);
        List<Long> problemIds = problemKnowledgeUnitPOList.stream().map(ProblemKnowledgeUnitPO::getProblemId).toList();
        return Map.of("problemIds", problemIds);
    }

    @Override
    public Map<String, Object> addProblemKnowledgeUnit(ProblemKnowledgeUnitBO problemKnowledgeUnitBO) {

        ProblemKnowledgeUnitPO problemKnowledgeUnitPO = ProblemKnowledgeUnitBO.toPO(problemKnowledgeUnitBO);
        if(problemKnowledgeUnitMapper.selectCount(new QueryWrapper<ProblemKnowledgeUnitPO>().eq("problem_id", problemKnowledgeUnitPO.getProblemId()).eq("knowledge_unit_id", problemKnowledgeUnitPO.getKnowledgeUnitId())) > 0){
            return Map.of("添加失败，该关系已存在", false);
        }
        baseMapper.insert(problemKnowledgeUnitPO);
        return Map.of("problemKnowledgeUnitId", problemKnowledgeUnitPO.getId());
    }

    @Override
    public Map<String, Object> deleteProblemKnowledgeUnit(Long problemId, Long knowledgeUnitId) {
        if(problemId == null && knowledgeUnitId == null){
            return Map.of("两者id均为空，禁止操作", false);
        }
        // 动态构建查询条件
        LambdaQueryWrapper<ProblemKnowledgeUnitPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(problemId != null, ProblemKnowledgeUnitPO::getProblemId, problemId)
                .eq(knowledgeUnitId != null, ProblemKnowledgeUnitPO::getKnowledgeUnitId, knowledgeUnitId);

        // 执行删除
        baseMapper.delete(wrapper);
        return Map.of("success", true);
    }

    @Override
    public Map<String, Object> batchAddProblemKnowledgeUnits(List<ProblemKnowledgeUnitBO> problemKnowledgeUnitBOList) {
        List<Long> successIds = new ArrayList<>();
        List<Long> failedIds = new ArrayList<>();
        for(ProblemKnowledgeUnitBO BO : problemKnowledgeUnitBOList){
            ProblemKnowledgeUnitPO PO = ProblemKnowledgeUnitBO.toPO(BO);
            PO.setId( null);
            if (problemKnowledgeUnitMapper.selectCount(new QueryWrapper<ProblemKnowledgeUnitPO>().eq("problem_id", PO.getProblemId()).eq("knowledge_unit_id", PO.getKnowledgeUnitId())) > 0) {
                failedIds.add(PO.getProblemId());
            }
            else {
                int insert = baseMapper.insert(PO);
                if (insert > 0) {
                    successIds.add(PO.getProblemId());
                } else {
                    failedIds.add(PO.getProblemId());
                }
            }
        }
        return Map.of( "successCount", successIds.size(), "failedCount", failedIds.size(), "failedIds", failedIds);

    }


}
