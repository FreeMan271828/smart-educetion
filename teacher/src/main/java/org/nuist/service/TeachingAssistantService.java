package org.nuist.service;

import org.nuist.business_object.QuestionBO;

import java.util.List;
import java.util.Map;

public interface TeachingAssistantService {
    /**
     * 根据知识点，自动生成符合需求的课后考题
     *
     * @param knowledgeId  对应的知识点
     * @param questionType 所需要的问题类型
     * @param difficulty   所需要的难度
     * @return 生成的考题
     */
    QuestionBO generateQuestion(Long knowledgeId, String questionType, String difficulty);

    /**
     * 评测学生回答
     *
     * @param questionId 目标考题ID
     * @param studentId  作答学生ID
     * @param answer     学生作答内容
     * @return 评测结果
     */
    Map<String, Object> evaluateQuestionAnswer(Long questionId, Long studentId, String answer);

    /**
     * 生成教案
     * @param subjectType 学科类型
     * @param courseOutline 课程大纲
     * @param courseDocuments 课程相关文档
     * @param duration 目标课程时长（可选，单位分钟）
     * @param difficulty 难度级别
     * @param teachingStyle 教学风格（可选）
     * @return 生成的教案
     */
    Map<String, Object> generateTeachingPlan(
            String subjectType, String courseOutline, List<String> courseDocuments,
            Integer duration, String difficulty, String teachingStyle);
}
