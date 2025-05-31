package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.nuist.bo.CourseBO;
import org.nuist.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程信息管理控制器
 */
@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/course")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    /**
     * 根据课程ID获取课程信息
     * @param courseId 课程ID
     * @return 课程信息
     */
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseBO> getCourseById(@PathVariable("courseId") Long courseId) {
        CourseBO course = courseService.getCourseById(courseId);
        if (course != null) {
            return ResponseEntity.ok(course);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据课程名称获取课程信息
     * @param name 课程名称
     * @return 课程信息
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<CourseBO> getCourseByName(@PathVariable("name") String name) {
        CourseBO course = courseService.getCourseByName(name);
        if (course != null) {
            return ResponseEntity.ok(course);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据课程代码获取课程信息
     * @param code 课程代码
     * @return 课程信息
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<CourseBO> getCourseByCode(@PathVariable("code") String code) {
        CourseBO course = courseService.getCourseByCode(code);
        if (course != null) {
            return ResponseEntity.ok(course);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 模糊查询课程
     * @param name 课程名称关键词
     * @return 课程列表
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<CourseBO>> getCoursesByNameLike(@RequestParam("name") String name) {
        List<CourseBO> courses = courseService.getCoursesByNameLike(name);
        return ResponseEntity.ok(courses);
    }
    
    /**
     * 按分类查询课程
     * @param category 课程分类
     * @return 课程列表
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<CourseBO>> getCoursesByCategory(@PathVariable("category") String category) {
        List<CourseBO> courses = courseService.getCoursesByCategory(category);
        return ResponseEntity.ok(courses);
    }
    
    /**
     * 获取所有课程
     * @return 课程列表
     */
    @GetMapping("/all")
    public ResponseEntity<List<CourseBO>> getAllCourses() {
        List<CourseBO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
    /**
     * 保存或更新课程信息
     * @param course 课程信息
     * @return 保存结果
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveOrUpdateCourse(@RequestBody CourseBO course) {
        Long courseId = courseService.saveOrUpdateCourse(course);
        
        Map<String, Object> result = new HashMap<>();
        if (courseId != null) {
            result.put("success", true);
            result.put("courseId", courseId);
            result.put("message", "课程信息保存成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "课程信息保存失败");
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    /**
     * 删除课程
     * @param courseId 课程ID
     * @return 删除结果
     */
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Map<String, Object>> deleteCourse(@PathVariable("courseId") Long courseId) {
        boolean success = courseService.deleteCourse(courseId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "课程删除成功" : "课程删除失败");
        return ResponseEntity.ok(result);
    }
    
    /**
     * 批量删除课程
     * @param courseIds 课程ID列表
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCourses(@RequestBody List<Long> courseIds) {
        int count = courseService.batchDeleteCourses(courseIds);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", count > 0);
        result.put("count", count);
        result.put("message", count > 0 ? "批量删除课程成功" : "批量删除课程失败");
        return ResponseEntity.ok(result);
    }
    
    /**
     * 综合搜索课程
     * @param keywords 关键词
     * @param category 课程分类（可选）
     * @return 课程列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<CourseBO>> searchCourses(
            @RequestParam("keywords") String keywords,
            @RequestParam(value = "category", required = false) String category) {
        List<CourseBO> courses = courseService.searchCourses(keywords, category);
        return ResponseEntity.ok(courses);
    }
} 