package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.nuist.service.StudentAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 学生学习助手控制器
 */
@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/student-assistant")
public class StudentAssistantController {
    
    @Autowired
    private StudentAssistantService studentAssistantService;
    
    /**
     * 智能问答
     * @param studentId 学生ID
     * @param question 问题内容
     * @param courseId 相关课程ID(可选)
     * @return 问答结果
     */
    @PostMapping("/student/{studentId}/ask")
    public ResponseEntity<Map<String, Object>> askQuestion(
            @PathVariable("studentId") Long studentId,
            @RequestParam("question") String question,
            @RequestParam(value = "courseId", required = false) Long courseId) {
        Map<String, Object> answer = studentAssistantService.askQuestion(studentId, question, courseId);
        return ResponseEntity.ok(answer);
    }
    
    /**
     * 基于课程名称的智能问答
     * @param studentId 学生ID
     * @param question 问题内容
     * @param courseName 课程名称
     * @return 问答结果
     */
    @PostMapping("/student/{studentId}/ask/by-course-name")
    public ResponseEntity<Map<String, Object>> askQuestionByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("question") String question,
            @RequestParam("courseName") String courseName) {
        Map<String, Object> answer = studentAssistantService.askQuestionByCourseName(studentId, question, courseName);
        return ResponseEntity.ok(answer);
    }
    
    /**
     * 关联知识点的智能问答
     * @param studentId 学生ID
     * @param question 问题内容
     * @param knowledgeName 知识点名称
     * @return 问答结果
     */
    @PostMapping("/student/{studentId}/ask/by-knowledge")
    public ResponseEntity<Map<String, Object>> askQuestionByKnowledgeName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("question") String question,
            @RequestParam("knowledgeName") String knowledgeName) {
        Map<String, Object> answer = studentAssistantService.askQuestionByKnowledgeName(studentId, question, knowledgeName);
        return ResponseEntity.ok(answer);
    }
    
    /**
     * 生成个性化练习
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @param knowledgeIds 知识点ID列表(可选)
     * @param difficultyLevel 难度级别(可选)
     * @param questionCount 题目数量
     * @return 练习内容
     */
    @GetMapping("/student/{studentId}/generate-exercise")
    public ResponseEntity<Map<String, Object>> generateExercise(
            @PathVariable("studentId") Long studentId,
            @RequestParam("courseId") Long courseId,
            @RequestParam(value = "knowledgeIds", required = false) List<Long> knowledgeIds,
            @RequestParam(value = "difficultyLevel", required = false) String difficultyLevel,
            @RequestParam("questionCount") Integer questionCount) {
        Map<String, Object> exercise = studentAssistantService.generateExercise(studentId, courseId, knowledgeIds, 
                                                                           difficultyLevel, questionCount);
        return ResponseEntity.ok(exercise);
    }
    
    /**
     * 基于课程名称生成练习
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @param difficultyLevel 难度级别(可选)
     * @param questionCount 题目数量
     * @return 练习内容
     */
    @GetMapping("/student/{studentId}/generate-exercise/by-course")
    public ResponseEntity<Map<String, Object>> generateExerciseByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("courseName") String courseName,
            @RequestParam(value = "difficultyLevel", required = false) String difficultyLevel,
            @RequestParam("questionCount") Integer questionCount) {
        Map<String, Object> exercise = studentAssistantService.generateExerciseByCourseName(studentId, courseName, 
                                                                                      difficultyLevel, questionCount);
        return ResponseEntity.ok(exercise);
    }
    
    /**
     * 基于知识点名称生成练习
     * @param studentId 学生ID
     * @param knowledgeNames 知识点名称列表
     * @param difficultyLevel 难度级别(可选)
     * @param questionCount 题目数量
     * @return 练习内容
     */
    @GetMapping("/student/{studentId}/generate-exercise/by-knowledge")
    public ResponseEntity<Map<String, Object>> generateExerciseByKnowledgeNames(
            @PathVariable("studentId") Long studentId,
            @RequestParam("knowledgeNames") List<String> knowledgeNames,
            @RequestParam(value = "difficultyLevel", required = false) String difficultyLevel,
            @RequestParam("questionCount") Integer questionCount) {
        Map<String, Object> exercise = studentAssistantService.generateExerciseByKnowledgeNames(studentId, knowledgeNames, 
                                                                                          difficultyLevel, questionCount);
        return ResponseEntity.ok(exercise);
    }
    
    /**
     * 生成薄弱知识点练习
     * @param studentId 学生ID
     * @param questionCount 题目数量
     * @return 练习内容
     */
    @GetMapping("/student/{studentId}/generate-exercise/weak-points")
    public ResponseEntity<Map<String, Object>> generateWeakPointsExercise(
            @PathVariable("studentId") Long studentId,
            @RequestParam("questionCount") Integer questionCount) {
        Map<String, Object> exercise = studentAssistantService.generateWeakPointsExercise(studentId, questionCount);
        return ResponseEntity.ok(exercise);
    }
    
    /**
     * 提交练习答案
     * @param studentId 学生ID
     * @param exerciseId 练习ID
     * @param answers 答案内容
     * @return 练习评测结果
     */
    @PostMapping("/student/{studentId}/submit-answers")
    public ResponseEntity<Map<String, Object>> submitExerciseAnswers(
            @PathVariable("studentId") Long studentId,
            @RequestParam("exerciseId") String exerciseId,
            @RequestBody Map<String, String> answers) {
        Map<String, Object> result = studentAssistantService.submitExerciseAnswers(studentId, exerciseId, answers);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取实时错误纠正和提示
     * @param studentId 学生ID
     * @param questionId 问题ID
     * @param partialAnswer 部分答案
     * @return 纠正和提示信息
     */
    @PostMapping("/student/{studentId}/hint")
    public ResponseEntity<Map<String, Object>> getRealTimeHint(
            @PathVariable("studentId") Long studentId,
            @RequestParam("questionId") String questionId,
            @RequestParam("partialAnswer") String partialAnswer) {
        Map<String, Object> hint = studentAssistantService.getRealTimeHint(studentId, questionId, partialAnswer);
        return ResponseEntity.ok(hint);
    }
    
    /**
     * 搜索学习资源
     * @param studentId 学生ID
     * @param keywords 关键词
     * @param resourceType 资源类型(可选)
     * @param limit 数量限制
     * @return 资源列表
     */
    @GetMapping("/student/{studentId}/resources")
    public ResponseEntity<List<Map<String, Object>>> searchLearningResources(
            @PathVariable("studentId") Long studentId,
            @RequestParam("keywords") String keywords,
            @RequestParam(value = "resourceType", required = false) String resourceType,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        List<Map<String, Object>> resources = studentAssistantService.searchLearningResources(studentId, keywords, resourceType, limit);
        return ResponseEntity.ok(resources);
    }
    
    /**
     * 获取历史问答记录
     * @param studentId 学生ID
     * @param limit 数量限制
     * @return 历史问答记录
     */
    @GetMapping("/student/{studentId}/question-history")
    public ResponseEntity<List<Map<String, Object>>> getQuestionHistory(
            @PathVariable("studentId") Long studentId,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {
        List<Map<String, Object>> history = studentAssistantService.getQuestionHistory(studentId, limit);
        return ResponseEntity.ok(history);
    }
    
    /**
     * 搜索历史问答记录
     * @param studentId 学生ID
     * @param keywords 关键词
     * @param limit 数量限制
     * @return 匹配的历史问答记录
     */
    @GetMapping("/student/{studentId}/search-questions")
    public ResponseEntity<List<Map<String, Object>>> searchQuestionHistory(
            @PathVariable("studentId") Long studentId,
            @RequestParam("keywords") String keywords,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {
        List<Map<String, Object>> results = studentAssistantService.searchQuestionHistory(studentId, keywords, limit);
        return ResponseEntity.ok(results);
    }
} 