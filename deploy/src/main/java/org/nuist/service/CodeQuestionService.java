package org.nuist.service;

import org.nuist.bo.CodeQuestionBO;

import java.util.List;

public interface CodeQuestionService {

    /**
     * 持久化保存一个编程题目
     * @param codeQuestion 编程题目
     * @return 保存后的编程题目对象
     */
    CodeQuestionBO insertCodeQuestion(CodeQuestionBO codeQuestion);

    /**
     * 按照主键查找编程题目
     * @param codeQuestionId 编程题ID
     * @return 编程题目
     */
    CodeQuestionBO getCodeQuestionById(Long codeQuestionId);

    /**
     * 查找同属于一个Exam的所有编程题目
     * @param examId Exam主键
     * @return 题目列表
     */
    List<CodeQuestionBO> getCQuestionsInExam(Long examId);

    /**
     * 更新一个编程题目的信息
     * @param codeQuestion 附带更新内容的实体
     * @return 更新后的实体
     */
    CodeQuestionBO updateCodeQuestion(CodeQuestionBO codeQuestion);

    /**
     * 删除一道编程题目
     * @param codeQuestionId 编程题ID
     * @return 删除结果
     */
    boolean deleteCodeQuestionById(Long codeQuestionId);
}
