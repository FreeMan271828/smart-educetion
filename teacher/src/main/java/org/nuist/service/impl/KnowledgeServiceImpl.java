package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.nuist.business_object.KnowledgeBO;
import org.nuist.dto.AddKnowledgeDTO;
import org.nuist.dto.UpdateKnowledgeDTO;
import org.nuist.mapper.KnowledgeMapper;
import org.nuist.model.Knowledge;
import org.nuist.service.KnowledgeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class KnowledgeServiceImpl extends ServiceImpl<KnowledgeMapper, Knowledge> implements KnowledgeService {

    private final KnowledgeMapper knowledgeMapper;

    @Override
    public KnowledgeBO getKnowledgeById(Long id) {
        if (id == null) {
            return null;
        }
        Knowledge knowledge = knowledgeMapper.selectById(id);
        if (knowledge == null) {
            return null;
        }
        return KnowledgeBO.fromKnowledge(knowledge);
    }

    @Override
    public List<KnowledgeBO> getKnowledgeByCourseId(Long courseId) {
        if (courseId == null) {
            return new ArrayList<>();
        }
        return convertToKnowledgeBO(
                list(
                        Wrappers.<Knowledge>lambdaQuery().eq(Knowledge::getCourseId, courseId)));
    }

    @Override
    public List<KnowledgeBO> getKnowledgeByTeacherId(Long teacherId) {
        if (teacherId == null) {
            return new ArrayList<>();
        }
        return convertToKnowledgeBO(
                list(
                        Wrappers.<Knowledge>lambdaQuery().eq(Knowledge::getTeacherId, teacherId)));
    }

    @Override
    public List<KnowledgeBO> getKnowledgeByTeacherInCourse(Long courseId, Long teacherId) {
        if (courseId == null || teacherId == null) {
            return new ArrayList<>();
        }
        return convertToKnowledgeBO(
                list(
                        Wrappers.<Knowledge>lambdaQuery()
                                .eq(Knowledge::getTeacherId, teacherId)
                                .eq(Knowledge::getCourseId, courseId)));
    }

    @Override
    public KnowledgeBO saveKnowledge(AddKnowledgeDTO addKnowledgeDTO) {
        Knowledge knowledge = new Knowledge();
        knowledge.setName(addKnowledgeDTO.getName());
        knowledge.setDescription(addKnowledgeDTO.getDescription());
        knowledge.setDifficultyLevel(addKnowledgeDTO.getDifficultyLevel());
        knowledge.setCourseId(addKnowledgeDTO.getCourseId());
        knowledge.setTeacherId(addKnowledgeDTO.getTeacherId());
        knowledge.setTeachPlan(addKnowledgeDTO.getTeachPlan());

        knowledgeMapper.insert(knowledge);
        return KnowledgeBO.fromKnowledge(knowledge);
    }

    @Override
    public KnowledgeBO updateKnowledge(UpdateKnowledgeDTO updateKnowledgeDTO) {
        Knowledge knowledge = knowledgeMapper.selectById(updateKnowledgeDTO.getKnowledgeId());
        if (knowledge == null) {
            throw new IllegalArgumentException("Knowledge ID " + updateKnowledgeDTO.getKnowledgeId() + " not found");
        }

        if (StringUtils.hasText(updateKnowledgeDTO.getName())) {
            knowledge.setName(updateKnowledgeDTO.getName());
        }
        if (StringUtils.hasText(updateKnowledgeDTO.getDescription())) {
            knowledge.setDescription(updateKnowledgeDTO.getDescription());
        }
        if (StringUtils.hasText(updateKnowledgeDTO.getDifficultyLevel())) {
            knowledge.setDifficultyLevel(updateKnowledgeDTO.getDifficultyLevel());
        }
        if (StringUtils.hasText(updateKnowledgeDTO.getTeachPlan())) {
            knowledge.setTeachPlan(updateKnowledgeDTO.getTeachPlan());
        }

        knowledgeMapper.updateById(knowledge);
        return KnowledgeBO.fromKnowledge(knowledge);

    }

    @Override
    public boolean deleteKnowledge(Long id) {
        return knowledgeMapper.deleteById(id) > 0;
    }

    private List<KnowledgeBO> convertToKnowledgeBO(List<Knowledge> knowledge) {
        return knowledge.stream().map(KnowledgeBO::fromKnowledge).collect(Collectors.toList());
    }

}
