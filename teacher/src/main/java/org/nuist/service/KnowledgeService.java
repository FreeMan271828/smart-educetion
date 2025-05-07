package org.nuist.service;

import org.nuist.business_object.KnowledgeBO;
import org.nuist.dto.AddKnowledgeDTO;
import org.nuist.dto.UpdateKnowledgeDTO;

import java.util.List;

public interface KnowledgeService {
    /**
     * 根据ID获取单个知识点
     * @param id 知识点ID
     * @return 知识点业务对象
     */
    KnowledgeBO getKnowledgeById(Long id);

    /**
     * 查询课程中的全部知识点
     * @param courseId 目标课程ID
     * @return 知识点列表
     */
    List<KnowledgeBO> getKnowledgeByCourseId(Long courseId);

    /**
     * 查询教师负责的知识点
     * @param teacherId 教师ID
     * @return 知识点列表
     */
    List<KnowledgeBO> getKnowledgeByTeacherId(Long teacherId);

    /**
     * 查询教师在某课程中负责的全部知识点
     * @param courseId 课程ID
     * @param teacherId 教师ID
     * @return 知识点列表
     */
    List<KnowledgeBO> getKnowledgeByTeacherInCourse(Long courseId, Long teacherId);

    /**
     * 持久化保存知识点
     * @param addKnowledgeDTO dto
     * @return 知识点业务对象
     */
    KnowledgeBO saveKnowledge(AddKnowledgeDTO addKnowledgeDTO);

    /**
     * 更新一个已存在的知识点
     * @param updateKnowledgeDTO dto
     * @return 更新后的知识点对象
     */
    KnowledgeBO updateKnowledge(UpdateKnowledgeDTO updateKnowledgeDTO);

    /**
     * 删除一个知识点
     * @param id 知识点的ID
     * @return 是否删除成功
     */
    boolean deleteKnowledge(Long id);
}
