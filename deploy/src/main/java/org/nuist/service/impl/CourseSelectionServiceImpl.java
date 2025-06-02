package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nuist.bo.CourseBO;
import org.nuist.bo.StudentBO;
import org.nuist.mapper.CourseMapper;
import org.nuist.mapper.CourseSelectionMapper;
import org.nuist.mapper.StudentMapper;
import org.nuist.po.CoursePO;
import org.nuist.po.CourseSelectionPO;
import org.nuist.po.StudentPO;
import org.nuist.service.CourseSelectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  课程选课服务实现类
 */

@Service
public class CourseSelectionServiceImpl implements CourseSelectionService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    @Autowired
     private StudentMapper studentMapper;

    /**
     * 根据学生ID获取选课
     * @param studentId
     * @return
     */
    @Override
    public List<CourseBO> getCourseSelections(Long studentId) {
       // log.info("根据学生ID查询选课信息，studentId={}", studentId);
        // 1. 查询课程ID列表 - 使用通用方法
        LambdaQueryWrapper<CourseSelectionPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CourseSelectionPO::getCourseId)
                .eq(CourseSelectionPO::getStudentId, studentId);

        List<CourseSelectionPO> selections = courseSelectionMapper.selectList(wrapper);
        List<Long> courseIds = selections.stream()
                .map(CourseSelectionPO::getCourseId)
                .collect(Collectors.toList());

       // log.info("根据课程ID列表查询课程信息，courseIds={}", courseIds);

        List<CourseBO> courseBOList = new ArrayList<>();
        if (!courseIds.isEmpty()) {
            LambdaQueryWrapper<CoursePO> courseQuery = new LambdaQueryWrapper<>();
            courseQuery.in(CoursePO::getId, courseIds);
            List<CoursePO> coursePOList = courseMapper.selectList(courseQuery);
          //  log.info("根据课程ID列表查询课程信息，查询到 {} 条记录", coursePOList.size());

            // 手动映射属性
            courseBOList = coursePOList.stream()
                    .map(po -> {
                        // 创建业务对象并手动设置属性
                        CourseBO bo = new CourseBO();
                        bo.setId(po.getId());           // 明确设置ID
                        bo.setName(po.getName());       // 设置课程名称
                        bo.setCode(po.getCode());
                        bo.setDescription(po.getDescription());
                        bo.setCredit(po.getCredit());
                        bo.setCategory(po.getCategory());
                        bo.setCreateTime(po.getCreateTime());
                        bo.setUpdateTime(po.getUpdateTime());
                        bo.setStatus(po.getStatus());

               //         log.debug("转换课程：ID={}, 名称={}", po.getId(), po.getName());
                        return bo;
                    })
                    .collect(Collectors.toList());

          //  log.info("成功转换 {} 个课程信息", courseBOList.size());
         //   log.info("查询选课结果：{}", courseBOList);

            return courseBOList;
        }
        else{
             return Collections.emptyList();
        }
    }

    /**
     *  保存选课
     * @param studentId
     * @param courseId
     * @return
     */
    @Override
    public Long saveCourseSelection(Long studentId, Long courseId) {
        CourseSelectionPO courseSelectionPO = new CourseSelectionPO();
        courseSelectionPO.setStudentId(studentId);
         courseSelectionPO.setCourseId(courseId);
         courseSelectionPO.setCreatedTime(LocalDateTime.now());
        courseSelectionMapper.insert( courseSelectionPO);
        return  courseSelectionPO.getId();
    }

    /**
     *  删除选课
     * @param studentId
     * @param courseId
     * @return
     */
    @Override
    public boolean deleteCourseSelection(Long studentId, Long courseId) {

            // 使用类型安全的lambda条件构造器
            LambdaQueryWrapper<CourseSelectionPO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(CourseSelectionPO::getStudentId, studentId)
                    .eq(CourseSelectionPO::getCourseId, courseId);

            // 执行删除并返回结果
            return courseSelectionMapper.delete(wrapper) > 0;
    }

    /**
     *  根据课程ID获取选课学生
     * @param courseId
     * @return
     */
    @Override
    public List<StudentBO> getStudentsByCourseId(Long courseId) {
        // 1. 查询选课关系表获取学生ID列表 - 只使用selectList
        List<CourseSelectionPO> selections = courseSelectionMapper.selectList(
                new LambdaQueryWrapper<CourseSelectionPO>()
                        .select(CourseSelectionPO::getStudentId) // 只查询学生ID列
                        .eq(CourseSelectionPO::getCourseId, courseId)
        );

        // 2. 提取学生ID并去重
        List<Long> studentIds = selections.stream()
                .map(CourseSelectionPO::getStudentId)
                .distinct()
                .collect(Collectors.toList());

        // 3. 如果没有学生选课，返回空列表
        if (studentIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 4. 查询学生详细信息
        List<StudentPO> studentPOList = studentMapper.selectList(
                new LambdaQueryWrapper<StudentPO>()
                        .in(StudentPO::getStudentId, studentIds)
                        .select( // 明确指定返回字段
                                StudentPO::getStudentId,
                                StudentPO::getUsername,
                                StudentPO::getEmail,
                                StudentPO::getFullName,
                                StudentPO::getPhone,
                                StudentPO::getGrade,
                                StudentPO::getClassName,
                                StudentPO::getCreatedAt,
                                StudentPO::getUpdatedAt
                        )
                        .orderByDesc(StudentPO::getCreatedAt) // 按创建时间倒序排序
        );

        // 5. 转换为业务对象
        return studentPOList.stream()
                .map(po -> {
                    // 设置密码占位符，避免空指针异常
                    po.setPassword("******");
                    return StudentBO.fromPO(po);
                })
                .collect(Collectors.toList());
    }

    /**
     *  批量删除选课
     * @param courseId
     * @return
     */
    @Override
    public boolean DeleteAllCourseSelection(Long courseId) {
        LambdaQueryWrapper<CourseSelectionPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CourseSelectionPO::getCourseId, courseId);

        // 执行删除
        int deletedCount = courseSelectionMapper.delete(wrapper);

        return deletedCount > 0;
    }

    /**
     *  判断学生是否选该课程
     * @param studentId
     * @param courseId
     * @return
     */
    @Override
    public boolean isCourseSelected(Long studentId, Long courseId) {
        // 1. 构建查询条件
        LambdaQueryWrapper<CourseSelectionPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(CourseSelectionPO::getStudentId, studentId)
                .eq(CourseSelectionPO::getCourseId, courseId);

        // 2. 查询符合条件的记录数量
        Long count = courseSelectionMapper.selectCount(queryWrapper);

        // 3. 返回结果
        return count > 0;
    }


}
