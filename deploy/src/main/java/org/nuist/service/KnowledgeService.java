package org.nuist.service;

import org.nuist.bo.KnowledgeBO;
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
     * 将一个已存在的知识点添加到一个课程中
     * @param courseId 待添加知识点的课程
     * @param knowledgeId 目标知识点
     * @return 操作是否成功
     */
    boolean appendKnowledgeToCourse(Long courseId, Long knowledgeId);

    /**
     * 将一个已存在的知识点拷贝之后，加入到课程中
     * @param courseId 目标课程
     * @param knowledgeId 目标知识点
     * @return 复制后新知识点
     */
    KnowledgeBO copyKnowledgeToCourse(Long courseId, Long knowledgeId);

    /**
     * 重新排列一个课程中的知识点
     * @param knowledgeId 待重排的知识点ID
     * @param courseId 所属课程ID
     * @param position 重排后的新位置（从1开始计数）
     * @return 是否重排成功
     */
    boolean resortKnowledge(Long knowledgeId, Long courseId, Integer position);

    /**
     * 删除一个知识点
     *
     * @param id       知识点的ID
     * @param courseId 目标课程的ID
     * @return 是否删除成功
     */
    boolean deleteKnowledgeInCourse(Long id, Long courseId);
}
