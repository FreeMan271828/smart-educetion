package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.KnowledgeBO;
import org.nuist.dto.AddKnowledgeDTO;
import org.nuist.dto.UpdateKnowledgeDTO;
import org.nuist.mapper.CourseKnowledgeMapper;
import org.nuist.mapper.KnowledgeMapper;
import org.nuist.po.CourseKnowledge;
import org.nuist.po.Knowledge;
import org.nuist.service.KnowledgeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class KnowledgeServiceImpl extends ServiceImpl<KnowledgeMapper, Knowledge> implements KnowledgeService {

    private final KnowledgeMapper knowledgeMapper;
    private final CourseKnowledgeMapper courseKnowledgeMapper;

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
        return convertToKnowledgeBO(knowledgeMapper.selectKnowledgeInCourseOrdered(courseId));
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
                                .apply("knowledge_id IN (SELECT knowledge_id FROM course_knowledge WHERE course_id = {0})", courseId)
                )
        );
    }

    @Override
    public List<KnowledgeBO> searchKnowledge(String keyword) {
        return convertToKnowledgeBO(
                list(
                        Wrappers.<Knowledge>lambdaQuery()
                                .like(Knowledge::getName, keyword)
                                .or().like(Knowledge::getDescription, keyword)
                )
        );
    }

    @Override
    public KnowledgeBO saveKnowledge(AddKnowledgeDTO addKnowledgeDTO) {
        Knowledge knowledge = new Knowledge();
        knowledge.setName(addKnowledgeDTO.getName());
        knowledge.setDescription(addKnowledgeDTO.getDescription());
        knowledge.setDifficultyLevel(addKnowledgeDTO.getDifficultyLevel());
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
        if (updateKnowledgeDTO.getTeacherId() != null) {
            knowledge.setTeacherId(updateKnowledgeDTO.getTeacherId());
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
    public boolean resortKnowledge(Long knowledgeId, Long courseId, Integer position) {
        if (knowledgeId == null || courseId == null || position == null) {
            throw new IllegalArgumentException("Parameter cannot be null");
        }
        List<CourseKnowledge> cks = courseKnowledgeMapper.selectList(
                Wrappers.<CourseKnowledge>lambdaQuery()
                        .eq(CourseKnowledge::getCourseId, courseId)
                        .orderByAsc(CourseKnowledge::getSequenceNumber)
        );
        if (position < 1 || position > cks.size()) {
            throw new IllegalArgumentException("Position " + position + " not in range [1, " + cks.size() + "]");
        }
        // 找出要移动的项目的位置，并进行移动
        IntStream.range(0, cks.size())
                .filter(index -> cks.get(index).getKnowledgeId().equals(knowledgeId))
                .findFirst()
                .ifPresentOrElse(
                        oldIndex -> {
                            CourseKnowledge target = cks.remove(oldIndex);
                            cks.add(position - 1, target);  // 注意position是从1开始
                        },
                        () -> {
                            throw new IllegalArgumentException("Knowledge ID " + knowledgeId + " not found");
                        }
                );
        // 重排完毕后，重新设置列表的sequenceNumber
        normalizeSequenceNumber(cks);
        cks.forEach(courseKnowledgeMapper::updateById);
        return true;
    }

    @Override
    public boolean deleteKnowledgeInCourse(Long id, Long courseId) {
//        int knowledgeDeleteCount = knowledgeMapper.deleteById(id);
        int ckDeleteCount = courseKnowledgeMapper.delete(
                Wrappers.<CourseKnowledge>lambdaQuery()
                        .eq(CourseKnowledge::getKnowledgeId, id)
        );
        if (ckDeleteCount <= 0) {
            return false;
        }
        // 还要把剩余的知识点重排序
        postDeleteKnowledgeInCourse(courseId);

        return true;
    }

    @Override
    public boolean batchDeleteKnowledgeInCourse(List<Long> knowledgeIds, Long courseId) {
        int deleteCount = courseKnowledgeMapper.delete(
                Wrappers.<CourseKnowledge>lambdaQuery()
                        .eq(CourseKnowledge::getCourseId, courseId)
                        .in(CourseKnowledge::getKnowledgeId, knowledgeIds)
        );
        if (deleteCount <= 0) {
            return false;
        }
        postDeleteKnowledgeInCourse(courseId);
        return true;
    }

    @Override
    public boolean deleteKnowledge(Long knowledgeId) {
        boolean result = removeById(knowledgeId);
        if (!result) {
            return false;
        }
        // 此时删除所有与该知识点相关的课程关联
        courseKnowledgeMapper.delete(
                Wrappers.<CourseKnowledge>lambdaQuery()
                        .eq(CourseKnowledge::getKnowledgeId, knowledgeId)
        );
        return true;
    }

    @Override
    public boolean batchDeleteKnowledge(List<Long> knowledgeIds) {
        boolean result = removeBatchByIds(knowledgeIds);
        if (!result) {
            return false;
        }
        // 删除所有与这批知识点有关联的课程关联
        courseKnowledgeMapper.delete(
                Wrappers.<CourseKnowledge>lambdaQuery()
                        .in(CourseKnowledge::getKnowledgeId, knowledgeIds)
        );
        return true;
    }

    private List<KnowledgeBO> convertToKnowledgeBO(List<Knowledge> knowledge) {
        return knowledge.stream().map(KnowledgeBO::fromKnowledge).collect(Collectors.toList());
    }

    @Override
    public boolean appendKnowledgeToCourse(Long courseId, Long knowledgeId) {
        // 先获取当前课程中的Knowledge数量
        int knowledgeCount = Math.toIntExact(courseKnowledgeMapper.selectCount(
                Wrappers.<CourseKnowledge>lambdaQuery()
                        .eq(CourseKnowledge::getCourseId, courseId)
        ));
        // 维护多对多关系
        return courseKnowledgeMapper.insert(
                CourseKnowledge.builder()
                        .courseId(courseId)
                        .knowledgeId(knowledgeId)
                        .sequenceNumber(knowledgeCount + 1)
                        .build()
        ) > 0;
    }

    @Override
    public KnowledgeBO copyKnowledgeToCourse(Long courseId, Long knowledgeId) {
        if (courseId == null || knowledgeId == null) {
            throw new IllegalArgumentException("Parameter cannot be null");
        }
        Knowledge knowledge = knowledgeMapper.selectById(knowledgeId);
        if (knowledge == null) {
            throw new IllegalArgumentException("Knowledge ID " + knowledgeId + " not found");
        }
        Knowledge copy = new Knowledge();
        // 复制一个课程内容
        copy.setName(knowledge.getName());
        copy.setDescription(knowledge.getDescription());
        copy.setDifficultyLevel(knowledge.getDifficultyLevel());
        copy.setTeachPlan(knowledge.getTeachPlan());
        copy.setTeacherId(knowledge.getTeacherId());
        knowledgeMapper.insert(copy);
        // 再维护多对多关系
        appendKnowledgeToCourse(courseId, copy.getKnowledgeId());
        return KnowledgeBO.fromKnowledge(copy);
    }

    private void postDeleteKnowledgeInCourse(Long courseId) {
        // 在课程中删除知识点的后操作：重新排序
        List<CourseKnowledge> cks = courseKnowledgeMapper.selectList(
                Wrappers.<CourseKnowledge>lambdaQuery()
                        .eq(CourseKnowledge::getCourseId, courseId)
                        .orderByAsc(CourseKnowledge::getSequenceNumber)
        );
        normalizeSequenceNumber(cks);
        cks.forEach(courseKnowledgeMapper::updateById);
    }

    private void checkAndRemoveIsolatedKnowledge(Long knowledgeId) {
        // 检查一个知识点是否没有被任何课程引用。如果是，则移除该知识点
        List<CourseKnowledge> cks = courseKnowledgeMapper.selectList(
                Wrappers.<CourseKnowledge>lambdaQuery()
                        .eq(CourseKnowledge::getKnowledgeId, knowledgeId)
        );
        if (cks.isEmpty()) {
            knowledgeMapper.deleteById(knowledgeId);
        }
    }

    private void normalizeSequenceNumber(List<CourseKnowledge> cks) {
        for (int index = 0; index < cks.size(); index++) {
            CourseKnowledge ck = cks.get(index);
            ck.setSequenceNumber(index + 1);
            cks.set(index, ck);
        }
    }

}
