package org.nuist.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 学习计划feign客户端
 */
@FeignClient(value = "learningPlanService")
public interface LearningPlanClient {

    /**
     * 生成个性化学习计划
     * @param studentId 学生ID
     * @param targetGoal 学习目标
     * @param timeFrame 时间范围（天数）
     * @param courseIds 课程ID列表（可选）
     * @param knowledgeIds 知识点ID列表（可选）
     * @return 学习计划
     */
    @GetMapping("/api/learning-plan/student/{studentId}/generate")
    ResponseEntity<Map<String, Object>> generateLearningPlan(
            @PathVariable("studentId") Long studentId,
            @RequestParam("targetGoal") String targetGoal,
            @RequestParam("timeFrame") Integer timeFrame,
            @RequestParam(value = "courseIds", required = false) List<Long> courseIds,
            @RequestParam(value = "knowledgeIds", required = false) List<Long> knowledgeIds);
    
    /**
     * 根据课程名称生成学习计划
     * @param studentId 学生ID
     * @param targetGoal 学习目标
     * @param timeFrame 时间范围（天数）
     * @param courseNames 课程名称列表
     * @return 学习计划
     */
    @GetMapping("/api/learning-plan/student/{studentId}/generate/course-names")
    ResponseEntity<Map<String, Object>> generateLearningPlanByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("targetGoal") String targetGoal,
            @RequestParam("timeFrame") Integer timeFrame,
            @RequestParam("courseNames") List<String> courseNames);
    
    /**
     * 根据知识点名称生成学习计划
     * @param studentId 学生ID
     * @param targetGoal 学习目标
     * @param timeFrame 时间范围（天数）
     * @param knowledgeNames 知识点名称列表
     * @return 学习计划
     */
    @GetMapping("/api/learning-plan/student/{studentId}/generate/knowledge-names")
    ResponseEntity<Map<String, Object>> generateLearningPlanByKnowledgeName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("targetGoal") String targetGoal,
            @RequestParam("timeFrame") Integer timeFrame,
            @RequestParam("knowledgeNames") List<String> knowledgeNames);
    
    /**
     * 获取学生的当前学习计划
     * @param studentId 学生ID
     * @return 当前学习计划
     */
    @GetMapping("/api/learning-plan/student/{studentId}/current")
    ResponseEntity<Map<String, Object>> getCurrentLearningPlan(@PathVariable("studentId") Long studentId);
    
    /**
     * 获取学生的历史学习计划
     * @param studentId 学生ID
     * @return 历史学习计划列表
     */
    @GetMapping("/api/learning-plan/student/{studentId}/history")
    ResponseEntity<List<Map<String, Object>>> getLearningPlanHistory(@PathVariable("studentId") Long studentId);
    
    /**
     * 搜索学习计划
     * @param studentId 学生ID
     * @param keywords 关键词
     * @return 搜索结果
     */
    @GetMapping("/api/learning-plan/student/{studentId}/search")
    ResponseEntity<List<Map<String, Object>>> searchLearningPlans(
            @PathVariable("studentId") Long studentId,
            @RequestParam("keywords") String keywords);
    
    /**
     * 更新学习计划进度
     * @param planId 计划ID
     * @param activityId 活动ID
     * @param status 完成状态
     * @param feedback 反馈信息(可选)
     * @return 更新结果
     */
    @PutMapping("/api/learning-plan/update-progress")
    ResponseEntity<Map<String, Object>> updatePlanProgress(
            @RequestParam("planId") String planId,
            @RequestParam("activityId") String activityId,
            @RequestParam("status") String status,
            @RequestParam(value = "feedback", required = false) String feedback);
    
    /**
     * 获取特定日期的学习计划内容
     * @param studentId 学生ID
     * @param date 日期
     * @return 指定日期的学习计划内容
     */
    @GetMapping("/api/learning-plan/student/{studentId}/daily")
    ResponseEntity<List<Map<String, Object>>> getDailyPlanActivities(
            @PathVariable("studentId") Long studentId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);
    
    /**
     * 根据计划名称和日期获取学习计划内容
     * @param studentId 学生ID
     * @param planName 计划名称
     * @param date 日期
     * @return 学习计划内容
     */
    @GetMapping("/api/learning-plan/student/{studentId}/daily/by-name")
    ResponseEntity<List<Map<String, Object>>> getDailyPlanByName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("planName") String planName,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);
    
    /**
     * 获取学习计划推荐资源
     * @param planId 计划ID
     * @return 推荐资源列表
     */
    @GetMapping("/api/learning-plan/{planId}/resources")
    ResponseEntity<List<Map<String, Object>>> getPlanRecommendedResources(@PathVariable("planId") String planId);
}
