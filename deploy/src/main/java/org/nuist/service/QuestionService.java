package org.nuist.service;

import org.nuist.bo.QuestionBO;

import java.time.LocalDate;
import java.util.List;

public interface QuestionService {
    /**
     * 根据问题ID查找一个问题
     * @param id 问题ID
     * @return 问题对象
     */
    QuestionBO getQuestionById(Long id);

    /**
     * 查找一个教师创建的所有问题
     * @param teacherId 教师ID
     * @return 问题列表
     */
    List<QuestionBO> getQuestionsByTeacherId(Long teacherId);

    /**
     * 根据问题类型查找问题
     * @param questionType 问题类型
     * @return 问题列表
     */
    List<QuestionBO> getQuestionsByType(String questionType);

    /**
     * 根据难度级别查找问题
     * @param difficulty 难度级别
     * @return 问题列表
     */
    List<QuestionBO> getQuestionsByDifficulty(String difficulty);

    /**
     * 根据知识点ID查找问题
     * @param knowledgeId 知识点ID
     * @return 问题列表
     */
    List<QuestionBO> getQuestionsByKnowledgeId(Long knowledgeId);

    /**
     * 对于一个知识点中的问题，进行复杂条件查询
     *
     * @param knowledgeId 所属知识点ID
     * @param questionType 问题类型（可选）
     * @param difficulty 问题难度（可选）
     * @param startTime 问题开始时间（可选）
     * @param endTime 问题结束时间（可选）
     * @return 问题列表
     */
    List<QuestionBO> getQuestionsByConditionInKnowledge(
            Long knowledgeId,
            String questionType,
            String difficulty,
            LocalDate startTime,
            LocalDate endTime
    );

    /**
     * 保存一个问题
     * @param questionBO 问题业务对象
     * @return 保存后的问题对象（附带主键和时间字段）
     */
    QuestionBO saveQuestion(QuestionBO questionBO);

    /**
     * 更新一个问题
     * @param questionBO 问题业务对象
     * @return 更新后的问题对象
     */
    QuestionBO updateQuestion(QuestionBO questionBO);

    /**
     * 删除一个问题
     * @param id 问题ID
     * @return 是否成功删除
     */
    boolean deleteQuestion(Long id);
}