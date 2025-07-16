package org.nuist.service;

import org.nuist.bo.QuestionBO;

import java.util.List;
import java.util.Map;

public interface TeachingAssistantService {

    /**
     * 生成教案
     * @param subjectType 学科类型
     * @param courseOutline 课程大纲
     * @param duration 目标课程时长（可选，单位分钟）
     * @param difficulty 难度级别
     * @param teachingStyle 教学风格（可选）
     * @return 生成的教案
     */
    Map<String, Object> generateTeachingPlan(
            String subjectType, String courseOutline, Integer duration, String difficulty, String teachingStyle
    );

    Map<String, Object> improveTeachingPlan(String previousPlan, String suggestion);

    /**
     * AI生成学生在课程中的学习报告
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @return 学生学习报告
     */
    Map<String, Object> analyzeCourseLearning(Long courseId, Long studentId);
}
