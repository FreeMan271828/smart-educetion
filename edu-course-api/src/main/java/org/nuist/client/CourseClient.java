package org.nuist.client;

import org.nuist.bo.CourseBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 课程feign客户端
 */
@FeignClient(value = "courseService")
public interface CourseClient {

    /**
     * 根据课程ID获取课程信息
     * @param courseId 课程ID
     * @return 课程信息
     */
    @GetMapping("/api/course/{courseId}")
    ResponseEntity<CourseBO> getCourseById(@PathVariable("courseId") Long courseId);
    
    /**
     * 根据课程名称获取课程信息
     * @param name 课程名称
     * @return 课程信息
     */
    @GetMapping("/api/course/name/{name}")
    ResponseEntity<CourseBO> getCourseByName(@PathVariable("name") String name);
    
    /**
     * 根据课程代码获取课程信息
     * @param code 课程代码
     * @return 课程信息
     */
    @GetMapping("/api/course/code/{code}")
    ResponseEntity<CourseBO> getCourseByCode(@PathVariable("code") String code);
    
    /**
     * 模糊查询课程
     * @param name 课程名称关键词
     * @return 课程列表
     */
    @GetMapping("/api/course/search/name")
    ResponseEntity<List<CourseBO>> getCoursesByNameLike(@RequestParam("name") String name);
    
    /**
     * 按分类查询课程
     * @param category 课程分类
     * @return 课程列表
     */
    @GetMapping("/api/course/category/{category}")
    ResponseEntity<List<CourseBO>> getCoursesByCategory(@PathVariable("category") String category);
    
    /**
     * 获取所有课程
     * @return 课程列表
     */
    @GetMapping("/api/course/all")
    ResponseEntity<List<CourseBO>> getAllCourses();
    
    /**
     * 保存或更新课程信息
     * @param course 课程信息
     * @return 保存结果
     */
    @PostMapping("/api/course/save")
    ResponseEntity<Map<String, Object>> saveOrUpdateCourse(@RequestBody CourseBO course);
    
    /**
     * 删除课程
     * @param courseId 课程ID
     * @return 删除结果
     */
    @DeleteMapping("/api/course/{courseId}")
    ResponseEntity<Map<String, Object>> deleteCourse(@PathVariable("courseId") Long courseId);
    
    /**
     * 批量删除课程
     * @param courseIds 课程ID列表
     * @return 删除结果
     */
    @DeleteMapping("/api/course/batch")
    ResponseEntity<Map<String, Object>> batchDeleteCourses(@RequestBody List<Long> courseIds);
    
    /**
     * 综合搜索课程
     * @param keywords 关键词
     * @param category 课程分类（可选）
     * @return 课程列表
     */
    @GetMapping("/api/course/search")
    ResponseEntity<List<CourseBO>> searchCourses(
            @RequestParam("keywords") String keywords,
            @RequestParam(value = "category", required = false) String category);
}
