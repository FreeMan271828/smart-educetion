package org.nuist.client;

import org.nuist.bo.LearningProgressBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 学习进度feign 客户端
 */
@FeignClient(value = "learningProgressService")
public interface LearningProgressClient {

    /**
     * 获取学生的学习进度
     * @param studentId 学生ID
     * @return 学习进度列表
     */
    @GetMapping("/api/learning-progress/student/{studentId}")
    ResponseEntity<List<LearningProgressBO>> getStudentProgress(
            @PathVariable("studentId") Long studentId);
    
    /**
     * 获取学生特定课程的学习进度
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 学习进度列表
     */
    @GetMapping("/api/learning-progress/student/{studentId}/course/{courseId}")
    ResponseEntity<List<LearningProgressBO>> getStudentCourseProgress(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId);
    
    /**
     * 根据课程名称获取学习进度
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 学习进度列表
     */
    @GetMapping("/api/learning-progress/student/{studentId}/course")
    ResponseEntity<List<LearningProgressBO>> getStudentCourseProgressByName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("name") String courseName);
    
    /**
     * 获取学生对特定知识点的学习进度
     * @param studentId 学生ID
     * @param knowledgeId 知识点ID
     * @return 学习进度业务对象
     */
    @GetMapping("/api/learning-progress/student/{studentId}/knowledge/{knowledgeId}")
    ResponseEntity<LearningProgressBO> getStudentKnowledgeProgress(
            @PathVariable("studentId") Long studentId,
            @PathVariable("knowledgeId") Long knowledgeId);
    
    /**
     * 根据知识点名称模糊查询学习进度
     * @param studentId 学生ID
     * @param knowledgeName 知识点名称
     * @return 学习进度列表
     */
    @GetMapping("/api/learning-progress/student/{studentId}/knowledge")
    ResponseEntity<List<LearningProgressBO>> getStudentKnowledgeProgressByName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("name") String knowledgeName);
    
    /**
     * 更新学习进度
     * @param progressBO 学习进度业务对象
     * @return 更新后的学习进度ID
     */
    @PostMapping("/api/learning-progress/update")
    ResponseEntity<Long> updateLearningProgress(@RequestBody LearningProgressBO progressBO);
    
    /**
     * 批量更新学习进度
     * @param progressList 学习进度列表
     * @return 更新成功的数量
     */
    @PostMapping("/api/learning-progress/batch-update")
    ResponseEntity<Integer> batchUpdateLearningProgress(@RequestBody List<LearningProgressBO> progressList);
    
    /**
     * 获取学生的总体学习进度百分比
     * @param studentId 学生ID
     * @return 总体学习进度百分比
     */
    @GetMapping("/api/learning-progress/student/{studentId}/overall-progress")
    ResponseEntity<Map<String, Object>> getOverallProgress(
            @PathVariable("studentId") Long studentId);
    
    /**
     * 获取学生课程的学习进度
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 课程学习进度百分比
     */
    @GetMapping("/api/learning-progress/student/{studentId}/course/{courseId}/progress")
    ResponseEntity<Map<String, Object>> getCourseProgress(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId);
    
    /**
     * 根据课程名称获取学习进度
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 课程学习进度百分比
     */
    @GetMapping("/api/learning-progress/student/{studentId}/course/progress")
    ResponseEntity<Map<String, Object>> getCourseProgressByName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("name") String courseName);
    
    /**
     * 获取学习进度统计
     * @param studentId 学生ID
     * @return 学习进度统计
     */
    @GetMapping("/api/learning-progress/student/{studentId}/statistics")
    ResponseEntity<Map<String, Object>> getProgressStatistics(
            @PathVariable("studentId") Long studentId);
    
    /**
     * 根据课程名称获取学习进度统计
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 学习进度统计
     */
    @GetMapping("/api/learning-progress/student/{studentId}/course/statistics")
    ResponseEntity<Map<String, Object>> getProgressStatisticsByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("name") String courseName);
}
