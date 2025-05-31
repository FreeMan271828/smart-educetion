package org.nuist.service;

import org.nuist.bo.CourseBO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程服务接口
 */
public interface CourseService {
    
    /**
     * 根据ID获取课程信息
     * @param courseId 课程ID
     * @return 课程信息
     */
    CourseBO getCourseById(Long courseId);
    
    /**
     * 根据课程名称查询课程
     * @param courseName 课程名称
     * @return 课程信息
     */
    CourseBO getCourseByName(String courseName);
    
    /**
     * 根据课程代码查询课程
     * @param courseCode 课程代码
     * @return 课程信息
     */
    CourseBO getCourseByCode(String courseCode);
    
    /**
     * 按名称模糊查询课程
     * @param nameLike 课程名称关键词
     * @return 课程列表
     */
    List<CourseBO> getCoursesByNameLike(String nameLike);
    
    /**
     * 按分类查询课程
     * @param category 课程分类
     * @return 课程列表
     */
    List<CourseBO> getCoursesByCategory(String category);
    
    /**
     * 获取所有课程
     * @return 课程列表
     */
    List<CourseBO> getAllCourses();
    
    /**
     * 保存或更新课程
     * @param course 课程信息
     * @return 课程ID
     */
    @Transactional
    Long saveOrUpdateCourse(CourseBO course);
    
    /**
     * 根据ID删除课程
     * @param courseId 课程ID
     * @return 是否成功
     */
    @Transactional
    boolean deleteCourse(Long courseId);
    
    /**
     * 批量删除课程
     * @param courseIds 课程ID列表
     * @return 删除数量
     */
    @Transactional
    int batchDeleteCourses(List<Long> courseIds);
    
    /**
     * 综合搜索课程
     * @param keywords 关键词
     * @param category 分类（可选）
     * @return 课程列表
     */
    List<CourseBO> searchCourses(String keywords, String category);
} 