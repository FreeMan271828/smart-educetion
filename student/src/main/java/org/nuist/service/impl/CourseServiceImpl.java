package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nuist.business_object.CourseBO;
import org.nuist.dao.CourseMapper;
import org.nuist.persist_object.CoursePO;
import org.nuist.service.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程服务实现类
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, CoursePO> implements CourseService {
    
    @Override
    public CourseBO getCourseById(Long courseId) {
        if (courseId == null) {
            return null;
        }
        
        CoursePO coursePO = getById(courseId);
        return convertToCourseBO(coursePO);
    }
    
    @Override
    public CourseBO getCourseByName(String courseName) {
        if (!StringUtils.hasText(courseName)) {
            return null;
        }
        
        LambdaQueryWrapper<CoursePO> queryWrapper = Wrappers.<CoursePO>lambdaQuery()
                .eq(CoursePO::getName, courseName);
        
        CoursePO coursePO = getOne(queryWrapper);
        return convertToCourseBO(coursePO);
    }
    
    @Override
    public CourseBO getCourseByCode(String courseCode) {
        if (!StringUtils.hasText(courseCode)) {
            return null;
        }
        
        LambdaQueryWrapper<CoursePO> queryWrapper = Wrappers.<CoursePO>lambdaQuery()
                .eq(CoursePO::getCode, courseCode);
        
        CoursePO coursePO = getOne(queryWrapper);
        return convertToCourseBO(coursePO);
    }
    
    @Override
    public List<CourseBO> getCoursesByNameLike(String nameLike) {
        if (!StringUtils.hasText(nameLike)) {
            return Collections.emptyList();
        }
        
        LambdaQueryWrapper<CoursePO> queryWrapper = Wrappers.<CoursePO>lambdaQuery()
                .like(CoursePO::getName, nameLike);
        
        List<CoursePO> coursePOList = list(queryWrapper);
        return convertToCourseBOList(coursePOList);
    }
    
    @Override
    public List<CourseBO> getCoursesByCategory(String category) {
        if (!StringUtils.hasText(category)) {
            return Collections.emptyList();
        }
        
        LambdaQueryWrapper<CoursePO> queryWrapper = Wrappers.<CoursePO>lambdaQuery()
                .eq(CoursePO::getCategory, category);
        
        List<CoursePO> coursePOList = list(queryWrapper);
        return convertToCourseBOList(coursePOList);
    }
    
    @Override
    public List<CourseBO> getAllCourses() {
        List<CoursePO> coursePOList = list();
        return convertToCourseBOList(coursePOList);
    }
    
    @Override
    @Transactional
    public Long saveOrUpdateCourse(CourseBO course) {
        if (course == null) {
            return null;
        }
        
        CoursePO coursePO = convertToCoursePO(course);
        
        // 新增或更新时间
        LocalDateTime now = LocalDateTime.now();
        if (coursePO.getId() == null) {
            coursePO.setCreateTime(now);
        }
        coursePO.setUpdateTime(now);
        
        // 保存或更新
        saveOrUpdate(coursePO);
        
        return coursePO.getId();
    }
    
    @Override
    @Transactional
    public boolean deleteCourse(Long courseId) {
        if (courseId == null) {
            return false;
        }
        
        return removeById(courseId);
    }
    
    @Override
    @Transactional
    public int batchDeleteCourses(List<Long> courseIds) {
        if (courseIds == null || courseIds.isEmpty()) {
            return 0;
        }
        
        return baseMapper.deleteBatchIds(courseIds);
    }
    
    @Override
    public List<CourseBO> searchCourses(String keywords, String category) {
        LambdaQueryWrapper<CoursePO> queryWrapper = Wrappers.<CoursePO>lambdaQuery();
        
        if (StringUtils.hasText(keywords)) {
            queryWrapper.and(wrapper -> wrapper
                    .like(CoursePO::getName, keywords)
                    .or()
                    .like(CoursePO::getCode, keywords)
                    .or()
                    .like(CoursePO::getDescription, keywords)
            );
        }
        
        if (StringUtils.hasText(category)) {
            queryWrapper.eq(CoursePO::getCategory, category);
        }
        
        List<CoursePO> coursePOList = list(queryWrapper);
        return convertToCourseBOList(coursePOList);
    }
    
    /**
     * 将CoursePO转换为CourseBO
     * @param coursePO 课程持久化对象
     * @return 课程业务对象
     */
    private CourseBO convertToCourseBO(CoursePO coursePO) {
        if (coursePO == null) {
            return null;
        }
        
        CourseBO courseBO = new CourseBO();
        BeanUtils.copyProperties(coursePO, courseBO);
        return courseBO;
    }
    
    /**
     * 将CoursePO列表转换为CourseBO列表
     * @param coursePOList 课程持久化对象列表
     * @return 课程业务对象列表
     */
    private List<CourseBO> convertToCourseBOList(List<CoursePO> coursePOList) {
        if (coursePOList == null || coursePOList.isEmpty()) {
            return Collections.emptyList();
        }
        
        return coursePOList.stream()
                .map(this::convertToCourseBO)
                .collect(Collectors.toList());
    }
    
    /**
     * 将CourseBO转换为CoursePO
     * @param courseBO 课程业务对象
     * @return 课程持久化对象
     */
    private CoursePO convertToCoursePO(CourseBO courseBO) {
        if (courseBO == null) {
            return null;
        }
        
        CoursePO coursePO = new CoursePO();
        BeanUtils.copyProperties(courseBO, coursePO);
        return coursePO;
    }
} 