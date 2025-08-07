package org.nuist.service;

import org.nuist.bo.CodeQuestionAnswerBO;

import java.util.List;

public interface CodeQuestionAnswerService {

    /**
     * 根据ID获取作答评测记录
     * @param id 作答ID
     * @return 编程题作答对象
     */
    CodeQuestionAnswerBO getCQuestionAnswer(Long id);

    /**
     * 查看一道编程题的所有作答记录
     * @param codeQuestionId 编程题ID
     * @return 结果列表
     */
    List<CodeQuestionAnswerBO> getAnswersInCQuestion(Long codeQuestionId);

    /**
     * 查看一个学生的所有编程题提交记录
     * @param studentId 学生ID
     * @return 结果列表
     */
    List<CodeQuestionAnswerBO> getAnswersByStudent(Long studentId);

    /**
     * 查看一个学生在一道编程题里的所有提交记录
     * @param codeQuestionId 编程题ID
     * @param studentId 学生ID
     * @return 结果列表
     */
    List<CodeQuestionAnswerBO> getStudentAnswersInCQuestion(Long codeQuestionId, Long studentId);

    /**
     * 获取学生在一道编程题里得分最高的提交
     * @param codeQuestionId 编程题ID
     * @param studentId 学生ID
     * @return 结果
     */
    CodeQuestionAnswerBO getStudentBestAnswerInCQuestion(Long codeQuestionId, Long studentId);

    /**
     * 获取学生在一个Exam的所有题目的作答记录
     * @param examId 测验ID
     * @param studentId 学生ID
     * @param best 是否仅取出每题的最优记录
     * @return 结果列表
     */
    List<CodeQuestionAnswerBO> getAnswersInExam(Long examId, Long studentId, boolean best);

    /**
     * 检测一个学生是否已经AC了一道编程题
     * @param studentId 学生ID
     * @param codeQuestionId 编程题ID
     * @return 是否AC
     */
    boolean isAccepted(Long studentId, Long codeQuestionId);

    /**
     * 提交作答
     * @param studentId 学生ID
     * @param codeQuestionId 题目ID
     * @param studentCode 提交代码内容
     * @param language 编程语言
     * @return 作答评测结果
     */
    CodeQuestionAnswerBO submit(Long studentId, Long codeQuestionId, String studentCode, String language);
}
