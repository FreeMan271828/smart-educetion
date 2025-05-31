package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.nuist.service.LearningPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 学习计划控制器
 */
@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/learning-plan")
public class LearningPlanController  {
    
    @Autowired
    private LearningPlanService learningPlanService;
    
    /**
     * 生成个性化学习计划
     * @param studentId 学生ID
     * @param targetGoal 学习目标
     * @param timeFrame 时间范围（天数）
     * @param courseIds 课程ID列表（可选）
     * @param knowledgeIds 知识点ID列表（可选）
     * @return 学习计划
     */
    @GetMapping("/student/{studentId}/generate")
    public ResponseEntity<Map<String, Object>> generateLearningPlan(
            @PathVariable("studentId") Long studentId,
            @RequestParam("targetGoal") String targetGoal,
            @RequestParam("timeFrame") Integer timeFrame,
            @RequestParam(value = "courseIds", required = false) List<Long> courseIds,
            @RequestParam(value = "knowledgeIds", required = false) List<Long> knowledgeIds) {
        Map<String, Object> plan = learningPlanService.generateLearningPlan(studentId, targetGoal, timeFrame, courseIds, knowledgeIds);
        return ResponseEntity.ok(plan);
    }
    
    /**
     * 根据课程名称生成学习计划
     * @param studentId 学生ID
     * @param targetGoal 学习目标
     * @param timeFrame 时间范围（天数）
     * @param courseNames 课程名称列表
     * @return 学习计划
     */
    @GetMapping("/student/{studentId}/generate/course-names")
    public ResponseEntity<Map<String, Object>> generateLearningPlanByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("targetGoal") String targetGoal,
            @RequestParam("timeFrame") Integer timeFrame,
            @RequestParam("courseNames") List<String> courseNames) {
        Map<String, Object> plan = learningPlanService.generateLearningPlanByCourseName(studentId, targetGoal, timeFrame, courseNames);
        return ResponseEntity.ok(plan);
    }
    
    /**
     * 根据知识点名称生成学习计划
     * @param studentId 学生ID
     * @param targetGoal 学习目标
     * @param timeFrame 时间范围（天数）
     * @param knowledgeNames 知识点名称列表
     * @return 学习计划
     */
    @GetMapping("/student/{studentId}/generate/knowledge-names")
    public ResponseEntity<Map<String, Object>> generateLearningPlanByKnowledgeName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("targetGoal") String targetGoal,
            @RequestParam("timeFrame") Integer timeFrame,
            @RequestParam("knowledgeNames") List<String> knowledgeNames) {
        Map<String, Object> plan = learningPlanService.generateLearningPlanByKnowledgeName(studentId, targetGoal, timeFrame, knowledgeNames);
        return ResponseEntity.ok(plan);
    }
    
    /**
     * 获取学生的当前学习计划
     * @param studentId 学生ID
     * @return 当前学习计划
     */
    @GetMapping("/student/{studentId}/current")
    public ResponseEntity<Map<String, Object>> getCurrentLearningPlan(@PathVariable("studentId") Long studentId) {
        Map<String, Object> plan = learningPlanService.getCurrentLearningPlan(studentId);
        if (plan != null) {
            return ResponseEntity.ok(plan);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 获取学生的历史学习计划
     * @param studentId 学生ID
     * @return 历史学习计划列表
     */
    @GetMapping("/student/{studentId}/history")
    public ResponseEntity<List<Map<String, Object>>> getLearningPlanHistory(@PathVariable("studentId") Long studentId) {
        List<Map<String, Object>> plans = learningPlanService.getLearningPlanHistory(studentId);
        return ResponseEntity.ok(plans);
    }
    
    /**
     * 搜索学习计划
     * @param studentId 学生ID
     * @param keywords 关键词
     * @return 搜索结果
     */
    @GetMapping("/student/{studentId}/search")
    public ResponseEntity<List<Map<String, Object>>> searchLearningPlans(
            @PathVariable("studentId") Long studentId,
            @RequestParam("keywords") String keywords) {
        List<Map<String, Object>> plans = learningPlanService.searchLearningPlans(studentId, keywords);
        return ResponseEntity.ok(plans);
    }
    
    /**
     * 更新学习计划进度
     * @param planId 计划ID
     * @param activityId 活动ID
     * @param status 完成状态
     * @param feedback 反馈信息(可选)
     * @return 更新结果
     */
    @PutMapping("/update-progress")
    public ResponseEntity<Map<String, Object>> updatePlanProgress(
            @RequestParam("planId") String planId,
            @RequestParam("activityId") String activityId,
            @RequestParam("status") String status,
            @RequestParam(value = "feedback", required = false) String feedback) {
        Map<String, Object> result = learningPlanService.updatePlanProgress(planId, activityId, status, feedback);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取特定日期的学习计划内容
     * @param studentId 学生ID
     * @param date 日期
     * @return 指定日期的学习计划内容
     */
    @GetMapping("/student/{studentId}/daily")
    public ResponseEntity<List<Map<String, Object>>> getDailyPlanActivities(
            @PathVariable("studentId") Long studentId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Map<String, Object>> activities = learningPlanService.getDailyPlanActivities(studentId, date);
        return ResponseEntity.ok(activities);
    }
    
    /**
     * 根据计划名称和日期获取学习计划内容
     * @param studentId 学生ID
     * @param planName 计划名称
     * @param date 日期
     * @return 学习计划内容
     */
    @GetMapping("/student/{studentId}/daily/by-name")
    public ResponseEntity<List<Map<String, Object>>> getDailyPlanByName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("planName") String planName,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Map<String, Object>> activities = learningPlanService.getDailyPlanByName(studentId, planName, date);
        return ResponseEntity.ok(activities);
    }
    
    /**
     * 获取学习计划推荐资源
     * @param planId 计划ID
     * @return 推荐资源列表
     */
    @GetMapping("/{planId}/resources")
    public ResponseEntity<List<Map<String, Object>>> getPlanRecommendedResources(@PathVariable("planId") String planId) {
        List<Map<String, Object>> resources = learningPlanService.getPlanRecommendedResources(planId);
        return ResponseEntity.ok(resources);
    }
    
    /**
     * 根据计划名称获取推荐资源
     * @param studentId 学生ID
     * @param planName 计划名称
     * @return 推荐资源列表
     */
    @GetMapping("/student/{studentId}/resources/by-name")
    public ResponseEntity<List<Map<String, Object>>> getPlanResourcesByName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("planName") String planName) {
        List<Map<String, Object>> resources = learningPlanService.getPlanResourcesByName(studentId, planName);
        return ResponseEntity.ok(resources);
    }
    
    /**
     * 根据关键词搜索推荐资源
     * @param studentId 学生ID
     * @param keywords 关键词
     * @return 匹配的资源列表
     */
    @GetMapping("/student/{studentId}/resources/search")
    public ResponseEntity<List<Map<String, Object>>> searchPlanResources(
            @PathVariable("studentId") Long studentId,
            @RequestParam("keywords") String keywords) {
        List<Map<String, Object>> resources = learningPlanService.searchPlanResources(studentId, keywords);
        return ResponseEntity.ok(resources);
    }
} 