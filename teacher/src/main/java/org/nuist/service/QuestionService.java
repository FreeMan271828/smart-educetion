package org.nuist.service;

import org.nuist.business_object.QuestionBO;
import org.nuist.dto.AddQuestionDTO;
import org.nuist.dto.QuestionQueryDTO;
import org.nuist.dto.UpdateQuestionDTO;

import java.util.List;

public interface QuestionService {
    /**
     * 通过ID查找一个问题
     * @param id 问题ID
     * @return 问题的业务对象
     */
    QuestionBO getQuestionByID(Long id);

    /**
     * 根据所属知识点查找问题
     * @param knowledgeId 知识点ID
     * @return 问题列表
     */
    List<QuestionBO> getQuestionsInKnowledge(Long knowledgeId);

    /**
     * 查询教师负责的所有问题
     * @param teacherId 教师ID
     * @return 问题列表
     */
    List<QuestionBO> getQuestionsByTeacherId(Long teacherId);

    /**
     * 对于一个知识点中的问题，通过问题内容关键词搜索问题
     * @param keyword 关键词
     * @return 问题列表
     */
    List<QuestionBO> searchQuestionsByKeyword(Long knowledgeId, String keyword);

    /**
     * 对于一个知识点中的问题，进行复杂条件查询
     *
     * @param knowledgeId 所属知识点ID
     * @param queryDTO 查询条件
     * @return 问题列表
     */
    List<QuestionBO> getQuestionsByConditionInKnowledge(Long knowledgeId, QuestionQueryDTO queryDTO);

    /**
     * 持久化问题实体
     * @param addQuestionDTO dto
     * @return 持久化后的问题实体
     */
    QuestionBO saveQuestion(AddQuestionDTO addQuestionDTO);

    /**
     * 更新一个问题
     * @param updateQuestionDTO dto
     * @return 更新后的问题实体
     */
    QuestionBO updateQuestion(UpdateQuestionDTO updateQuestionDTO);

    /**
     * 删除一个问题
     * @param id 问题ID
     * @return 是否正确删除
     */
    boolean deleteQuestion(Long id);
}
