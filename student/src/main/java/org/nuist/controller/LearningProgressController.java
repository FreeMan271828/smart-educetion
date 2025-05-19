package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.nuist.business_object.LearningProgressBO;
import org.nuist.service.LearningProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习进度控制器
 */
@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/learning-progress")
public class LearningProgressController {
    
    @Autowired
    private LearningProgressService learningProgressService;
    
    /**
     * 获取学生的学习进度
     * @param studentId 学生ID
     * @return 学习进度列表
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<LearningProgressBO>> getStudentProgress(
            @PathVariable("studentId") Long studentId) {
        List<LearningProgressBO> progressList = learningProgressService.getStudentProgress(studentId);
        return ResponseEntity.ok(progressList);
    }
    
    /**
     * 获取学生特定课程的学习进度
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 学习进度列表
     */
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<LearningProgressBO>> getStudentCourseProgress(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId) {
        List<LearningProgressBO> progressList = learningProgressService.getStudentCourseProgress(studentId, courseId);
        return ResponseEntity.ok(progressList);
    }
    
    /**
     * 根据课程名称获取学习进度
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 学习进度列表
     */
    @GetMapping("/student/{studentId}/course/name")
    public ResponseEntity<List<LearningProgressBO>> getStudentCourseProgressByName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("name") String courseName) {
        List<LearningProgressBO> progressList = learningProgressService.getStudentCourseProgressByName(studentId, courseName);
        return ResponseEntity.ok(progressList);
    }
    
    /**
     * 获取学生对特定知识点的学习进度
     * @param studentId 学生ID
     * @param knowledgeId 知识点ID
     * @return 学习进度
     */
    @GetMapping("/student/{studentId}/knowledge/{knowledgeId}")
    public ResponseEntity<LearningProgressBO> getStudentKnowledgeProgress(
            @PathVariable("studentId") Long studentId,
            @PathVariable("knowledgeId") Long knowledgeId) {
        LearningProgressBO progress = learningProgressService.getStudentKnowledgeProgress(studentId, knowledgeId);
        if (progress != null) {
            return ResponseEntity.ok(progress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据知识点名称获取学习进度
     * @param studentId 学生ID
     * @param knowledgeName 知识点名称
     * @return 学习进度列表
     */
    @GetMapping("/student/{studentId}/knowledge/name")
    public ResponseEntity<List<LearningProgressBO>> getStudentKnowledgeProgressByName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("name") String knowledgeName) {
        List<LearningProgressBO> progressList = learningProgressService.getStudentKnowledgeProgressByName(studentId, knowledgeName);
        return ResponseEntity.ok(progressList);
    }
    
    /**
     * 更新学习进度
     * @param progress 学习进度
     * @return 更新结果
     */
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateLearningProgress(@RequestBody LearningProgressBO progress) {
        Long progressId = learningProgressService.updateLearningProgress(progress);
        
        Map<String, Object> result = new HashMap<>();
        if (progressId != null) {
            result.put("success", true);
            result.put("progressId", progressId);
            result.put("message", "学习进度更新成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "学习进度更新失败");
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    /**
     * 批量更新学习进度
     * @param progressList 学习进度列表
     * @return 更新结果
     */
    @PostMapping("/batch-update")
    public ResponseEntity<Map<String, Object>> batchUpdateLearningProgress(
            @RequestBody List<LearningProgressBO> progressList) {
        int count = learningProgressService.batchUpdateLearningProgress(progressList);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", count > 0);
        result.put("count", count);
        result.put("message", count > 0 ? "批量更新成功" : "批量更新失败");
        return ResponseEntity.ok(result);
    }
    
    /**
     * 更新知识点掌握度
     * @param studentId 学生ID
     * @param knowledgeId 知识点ID
     * @param score 得分
     * @return 更新后的学习进度
     */
    @PostMapping("/student/{studentId}/knowledge/{knowledgeId}/mastery")
    public ResponseEntity<LearningProgressBO> updateMasteryLevel(
            @PathVariable("studentId") Long studentId,
            @PathVariable("knowledgeId") Long knowledgeId,
            @RequestParam("score") BigDecimal score) {
        LearningProgressBO updatedProgress = learningProgressService.updateMasteryLevel(studentId, knowledgeId, score);
        
        if (updatedProgress != null) {
            return ResponseEntity.ok(updatedProgress);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 根据知识点名称更新掌握度
     * @param studentId 学生ID
     * @param knowledgeName 知识点名称
     * @param score 得分
     * @return 更新后的学习进度
     */
    @PostMapping("/student/{studentId}/knowledge/mastery")
    public ResponseEntity<LearningProgressBO> updateMasteryLevelByName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("name") String knowledgeName,
            @RequestParam("score") BigDecimal score) {
        LearningProgressBO updatedProgress = learningProgressService.updateMasteryLevelByName(studentId, knowledgeName, score);
        
        if (updatedProgress != null) {
            return ResponseEntity.ok(updatedProgress);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 获取学生的总体学习进度
     * @param studentId 学生ID
     * @return 总体学习进度百分比
     */
    @GetMapping("/student/{studentId}/overall-progress")
    public ResponseEntity<Map<String, Object>> getOverallProgress(
            @PathVariable("studentId") Long studentId) {
        BigDecimal progress = learningProgressService.getOverallProgress(studentId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("overallProgress", progress);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取学生课程的学习进度
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 课程学习进度百分比
     */
    @GetMapping("/student/{studentId}/course/{courseId}/progress")
    public ResponseEntity<Map<String, Object>> getCourseProgress(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId) {
        BigDecimal progress = learningProgressService.getCourseProgress(studentId, courseId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("courseId", courseId);
        result.put("courseProgress", progress);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 根据课程名称获取学习进度
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 课程学习进度百分比
     */
    @GetMapping("/student/{studentId}/course/progress")
    public ResponseEntity<Map<String, Object>> getCourseProgressByName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("name") String courseName) {
        BigDecimal progress = learningProgressService.getCourseProgressByName(studentId, courseName);
        
        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("courseName", courseName);
        result.put("courseProgress", progress);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取学习进度统计
     * @param studentId 学生ID
     * @return 学习进度统计
     */
    @GetMapping("/student/{studentId}/statistics")
    public ResponseEntity<Map<String, Object>> getProgressStatistics(
            @PathVariable("studentId") Long studentId) {
        Map<String, Object> statistics = learningProgressService.getProgressStatistics(studentId);
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * 根据课程名称获取学习进度统计
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 学习进度统计
     */
    @GetMapping("/student/{studentId}/course/statistics")
    public ResponseEntity<Map<String, Object>> getProgressStatisticsByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("name") String courseName) {
        Map<String, Object> statistics = learningProgressService.getProgressStatisticsByCourseName(studentId, courseName);
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * 生成学习计划
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @param days 天数
     * @return 学习计划
     */
    @GetMapping("/student/{studentId}/course/{courseId}/plan")
    public ResponseEntity<Map<String, Object>> generateLearningPlan(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId,
            @RequestParam("days") Integer days) {
        Map<String, Object> plan = learningProgressService.generateLearningPlan(studentId, courseId, days);
        return ResponseEntity.ok(plan);
    }
    
    /**
     * 根据课程名称生成学习计划
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @param days 天数
     * @return 学习计划
     */
    @GetMapping("/student/{studentId}/course/plan")
    public ResponseEntity<Map<String, Object>> generateLearningPlanByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("name") String courseName,
            @RequestParam("days") Integer days) {
        Map<String, Object> plan = learningProgressService.generateLearningPlanByCourseName(studentId, courseName, days);
        return ResponseEntity.ok(plan);
    }
    
    /**
     * 获取推荐学习资源
     * @param studentId 学生ID
     * @param limit 数量限制
     * @return 推荐资源列表
     */
    @GetMapping("/student/{studentId}/resources")
    public ResponseEntity<List<Map<String, Object>>> getRecommendedResources(
            @PathVariable("studentId") Long studentId,
            @RequestParam(value = "limit", required = false) Integer limit) {
        List<Map<String, Object>> resources = learningProgressService.getRecommendedResources(studentId, limit);
        return ResponseEntity.ok(resources);
    }
    
    /**
     * 搜索推荐学习资源
     * @param studentId 学生ID
     * @param keywords 关键词
     * @param limit 数量限制
     * @return 推荐资源列表
     */
    @GetMapping("/student/{studentId}/resources/search")
    public ResponseEntity<List<Map<String, Object>>> searchRecommendedResources(
            @PathVariable("studentId") Long studentId,
            @RequestParam("keywords") String keywords,
            @RequestParam(value = "limit", required = false) Integer limit) {
        List<Map<String, Object>> resources = learningProgressService.searchRecommendedResources(studentId, keywords, limit);
        return ResponseEntity.ok(resources);
    }
} 