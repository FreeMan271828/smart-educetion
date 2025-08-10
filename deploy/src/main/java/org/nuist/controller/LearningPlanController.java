package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.nuist.bo.LearningPlanBO;
import org.nuist.mapper.LearningPlanMapper;
import org.nuist.po.LearningPlanPO;
import org.nuist.service.CourseService;
import org.nuist.service.KnowledgeService;
import org.nuist.service.LearningPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

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

    private final WebClient webClient;
    private final LearningPlanMapper learningPlanMapper;
    private final CourseService courseService;
    private final KnowledgeService knowledgeService;
    
    @Autowired
    private LearningPlanService learningPlanService;

    public LearningPlanController(WebClient webClient, LearningPlanMapper learningPlanMapper, CourseService courseService, KnowledgeService knowledgeService) {
        this.webClient = webClient;
        this.learningPlanMapper = learningPlanMapper;
        this.courseService = courseService;
        this.knowledgeService = knowledgeService;
    }

    /**
     * 生成个性化学习计划
     * @param studentId 学生ID
     * @param targetGoal 学习目标
     * @param timeFrame 时间范围（天数）

     * @return 学习计划
     */
    @Operation(summary = "ai生成学习计划,并持久化到数据库")
    @GetMapping("/student/{studentId}/generate")
    public ResponseEntity<Map<String, Object>> generateLearningPlan(
            @PathVariable("studentId") Long studentId,
            @RequestParam("targetGoal") String targetGoal,
            @RequestParam("timeFrame") Integer timeFrame,
            @RequestParam("courseId") Long courseId) { // 变更为单个courseId

        Map<String, Object> plan = learningPlanService.generateLearningPlan(
                studentId, targetGoal, timeFrame, courseId
        );
        return ResponseEntity.ok(plan);
    }


    @Operation(summary = "添加学习计划")
    @PostMapping("/add-plan/{studentId}")
    public ResponseEntity<LearningPlanBO> addPlan(
            @PathVariable("studentId") Long studentId,
            @RequestBody LearningPlanBO learningPlanBO) {
        return ResponseEntity.ok(learningPlanService.addPlan(learningPlanBO));
    }
    


    
    /**
     * 获取学生的当前学习计划
     * @param studentId 学生ID
     * @return 当前学习计划
     */
    @Operation(summary = "获取学生的当前学习计划")
    @GetMapping("/student/{studentId}/current")
    public ResponseEntity<List<LearningPlanBO>> getCurrentLearningPlan(@PathVariable("studentId") Long studentId) {
        List<LearningPlanBO> plans = learningPlanService.getCurrentLearningPlan(studentId);
        if (plans != null) {
            return ResponseEntity.ok(plans);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 获取学生的历史学习计划
     * @param studentId 学生ID
     * @return 历史学习计划列表
     */
    @Operation(summary = "获取学生的已经结束的学习计划")
    @GetMapping("/student/{studentId}/history")
    public ResponseEntity<List<LearningPlanBO>> getLearningPlanHistory(@PathVariable("studentId") Long studentId) {
        List<LearningPlanBO> plans = learningPlanService.getLearningPlanHistory(studentId);
        return ResponseEntity.ok(plans);
    }
    
    /**
     * 搜索学习计划
     * @param studentId 学生ID
     * @param keywords 关键词
     * @return 搜索结果
     */
    @Operation(summary = "按名称搜索学习计划")
    @GetMapping("/student/{studentId}/search")
    public ResponseEntity<List<LearningPlanBO>> searchLearningPlans(
            @PathVariable("studentId") Long studentId,
            @RequestParam("keywords") String keywords) {
        List<LearningPlanBO> plans = learningPlanService.searchLearningPlans(studentId, keywords);
        return ResponseEntity.ok(plans);
    }




    @Operation(summary = "查询某学生已完成的学习计划")
    @GetMapping("/student/{studentId}/complete")
    public ResponseEntity<List<LearningPlanBO>> getCompletedLearningPlans(@PathVariable("studentId") Long studentId) {
        List<LearningPlanBO> plans = learningPlanService.getifCompletedLearningPlans(studentId,true);
        return ResponseEntity.ok(plans);
    }

    @Operation(summary = "查询某学生未完成的学习计划")
    @GetMapping("/student/{studentId}/incomplete")
    public ResponseEntity<List<LearningPlanBO>> getIncompleteLearningPlans(@PathVariable("studentId") Long studentId) {
        List<LearningPlanBO> plans = learningPlanService.getifCompletedLearningPlans(studentId,false);
        return ResponseEntity.ok(plans);
    }


    
    /**
     * 更新学习计划

     * @return 更新
     */
    @Operation(summary = "更新学习计划,不更新planId和studentId")
    @PutMapping("/update-plan")
    public ResponseEntity<LearningPlanBO> updatePlan(
            @RequestBody LearningPlanBO learningPlanBO) {
        return ResponseEntity.ok(learningPlanService.updatePlan(learningPlanBO));
    }




    /**
     * 获取特定日期的学习计划内容
     * @param studentId 学生ID
     * @param date 日期
     * @return 指定日期的学习计划内容
     */
    @Operation(summary = "获取特定日期生效的学习计划内容")
    @GetMapping("/student/{studentId}/day")
    public ResponseEntity<List<LearningPlanBO>> getDailyPlanActivities(
            @PathVariable("studentId") Long studentId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LearningPlanBO> activities = learningPlanService.getDailyPlanActivities(studentId, date);
        return ResponseEntity.ok(activities);
    }


    /**
     * 删除学习计划
     * @param planId 计划ID
     * @return 删除结果
     */
    @Operation(summary = "删除学习计划")
    @DeleteMapping("/{planId}")
    public ResponseEntity<Boolean> deletePlan(@PathVariable("planId") Long planId) {
        boolean result = learningPlanService.deletePlan(planId);
        return ResponseEntity.ok(result);
    }
    
   /* *//**
     * 根据计划名称和日期获取学习计划内容
     * @param studentId 学生ID
     * @param planName 计划名称
     * @param date 日期
     * @return 学习计划内容
     *//*
    @GetMapping("/student/{studentId}/daily/by-name")
    public ResponseEntity<List<Map<String, Object>>> getDailyPlanByName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("planName") String planName,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Map<String, Object>> activities = learningPlanService.getDailyPlanByName(studentId, planName, date);
        return ResponseEntity.ok(activities);
    }*/
    
   /* *//**
     * 获取学习计划推荐资源
     * @param planId 计划ID
     * @return 推荐资源列表
     *//*
    @GetMapping("/{planId}/resources")
    public ResponseEntity<List<Map<String, Object>>> getPlanRecommendedResources(@PathVariable("planId") String planId) {
        List<Map<String, Object>> resources = learningPlanService.getPlanRecommendedResources(planId);
        return ResponseEntity.ok(resources);
    }
    
    *//**
     * 根据计划名称获取推荐资源
     * @param studentId 学生ID
     * @param planName 计划名称
     * @return 推荐资源列表
     *//*
    @GetMapping("/student/{studentId}/resources/by-name")
    public ResponseEntity<List<Map<String, Object>>> getPlanResourcesByName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("planName") String planName) {
        List<Map<String, Object>> resources = learningPlanService.getPlanResourcesByName(studentId, planName);
        return ResponseEntity.ok(resources);
    }
    
    *//**
     * 根据关键词搜索推荐资源
     * @param studentId 学生ID
     * @param keywords 关键词
     * @return 匹配的资源列表
     *//*
    @GetMapping("/student/{studentId}/resources/search")
    public ResponseEntity<List<Map<String, Object>>> searchPlanResources(
            @PathVariable("studentId") Long studentId,
            @RequestParam("keywords") String keywords) {
        List<Map<String, Object>> resources = learningPlanService.searchPlanResources(studentId, keywords);
        return ResponseEntity.ok(resources);
    }*/
} 