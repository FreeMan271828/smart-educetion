package org.nuist.service;

import java.util.List;
import java.util.Map;

/**
 * 学生在线学习助手服务接口
 */
public interface StudentAssistantService {
    
    /**
     * 智能问答
     * @param studentId 学生ID
     * @param question 问题内容
     * @param courseId 相关课程ID(可选)
     * @return 问答结果
     */
    Map<String, Object> askQuestion(Long studentId, String question, Long courseId);
    
    /**
     * 基于课程名称的智能问答
     * @param studentId 学生ID
     * @param question 问题内容
     * @param courseName 课程名称
     * @return 问答结果
     */
    Map<String, Object> askQuestionByCourseName(Long studentId, String question, String courseName);
    
    /**
     * 关联知识点的智能问答
     * @param studentId 学生ID
     * @param question 问题内容
     * @param knowledgeName 知识点名称
     * @return 问答结果
     */
    Map<String, Object> askQuestionByKnowledgeName(Long studentId, String question, String knowledgeName);
    
    /**
     * 生成个性化练习
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @param knowledgeIds 知识点ID列表(可选)
     * @param difficultyLevel 难度级别(可选)
     * @param questionCount 题目数量
     * @return 练习内容
     */
    Map<String, Object> generateExercise(Long studentId, Long courseId, List<Long> knowledgeIds, 
                                        String difficultyLevel, Integer questionCount);
    
    /**
     * 基于课程名称生成练习
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @param difficultyLevel 难度级别(可选)
     * @param questionCount 题目数量
     * @return 练习内容
     */
    Map<String, Object> generateExerciseByCourseName(Long studentId, String courseName, 
                                                   String difficultyLevel, Integer questionCount);
    
    /**
     * 基于知识点名称生成练习
     * @param studentId 学生ID
     * @param knowledgeNames 知识点名称列表
     * @param difficultyLevel 难度级别(可选)
     * @param questionCount 题目数量
     * @return 练习内容
     */
    Map<String, Object> generateExerciseByKnowledgeNames(Long studentId, List<String> knowledgeNames, 
                                                       String difficultyLevel, Integer questionCount);
    
    /**
     * 生成薄弱知识点练习
     * @param studentId 学生ID
     * @param questionCount 题目数量
     * @return 练习内容
     */
    Map<String, Object> generateWeakPointsExercise(Long studentId, Integer questionCount);
    
    /**
     * 提交练习答案
     * @param studentId 学生ID
     * @param exerciseId 练习ID
     * @param answers 答案内容
     * @return 练习评测结果
     */
    Map<String, Object> submitExerciseAnswers(Long studentId, String exerciseId, Map<String, String> answers);
    
    /**
     * 获取实时错误纠正和提示
     * @param studentId 学生ID
     * @param questionId 问题ID
     * @param partialAnswer 部分答案
     * @return 纠正和提示信息
     */
    Map<String, Object> getRealTimeHint(Long studentId, String questionId, String partialAnswer);
    
    /**
     * 搜索学习资源
     * @param studentId 学生ID
     * @param keywords 关键词
     * @param resourceType 资源类型(可选)
     * @param limit 数量限制
     * @return 资源列表
     */
    List<Map<String, Object>> searchLearningResources(Long studentId, String keywords, 
                                                    String resourceType, Integer limit);
    
    /**
     * 获取历史问答记录
     * @param studentId 学生ID
     * @param limit 数量限制
     * @return 历史问答记录
     */
    List<Map<String, Object>> getQuestionHistory(Long studentId, Integer limit);
    
    /**
     * 搜索历史问答记录
     * @param studentId 学生ID
     * @param keywords 关键词
     * @param limit 数量限制
     * @return 匹配的历史问答记录
     */
    List<Map<String, Object>> searchQuestionHistory(Long studentId, String keywords, Integer limit);
} 