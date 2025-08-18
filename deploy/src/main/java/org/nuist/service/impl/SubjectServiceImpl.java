package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nuist.bo.CourseBO;
import org.nuist.bo.SubjectBO;
import org.nuist.mapper.CourseMapper;
import org.nuist.mapper.SubjectCourseMapper;
import org.nuist.mapper.SubjectMapper;
import org.nuist.po.CoursePO;
import org.nuist.po.SubjectCoursePO;
import org.nuist.po.SubjectPO;
import org.nuist.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, SubjectPO> implements SubjectService {
    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private SubjectCourseMapper subjectCourseMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<SubjectBO> searchSubjects(String keywords) {
        LambdaQueryWrapper<SubjectPO> queryWrapper = Wrappers.<SubjectPO>lambdaQuery();
        queryWrapper.like(SubjectPO::getName, keywords);
        return subjectMapper.selectList(queryWrapper).stream().map(SubjectBO::fromSubjectPO).toList();
    }

    @Override
    public SubjectBO getSubjectById(Long id) {

        return SubjectBO.fromSubjectPO(subjectMapper.selectById(id));
    }

    @Override
    public Map<String,Object> addSubject(SubjectBO subjectBO) {

        if(subjectMapper.insert(SubjectBO.toSubjectPO(subjectBO))>0){
            return Map.of("message","添加成功");
        }
        else{
            return Map.of("message","添加失败");
    }

    }

    @Override
    public Map<String, Object> updateSubject(SubjectBO subjectBO) {

        if(subjectMapper.updateById(SubjectBO.toSubjectPO(subjectBO))>0){
            return Map.of("message","更新成功");
        }
        else{
            return Map.of("message","更新失败");
        }
    }

    @Override
    public Map<String, Object> deleteSubject(Long id) {

        if(subjectMapper.deleteById(id)>0){
            return Map.of("message","删除成功");
        }
        else{
            return Map.of("message","删除失败");
        }
    }

    @Override
    public Map<String, Object> addSubjectCourseRelation(Long subjectId, Long courseId) {
        SubjectCoursePO subjectCoursePO = SubjectCoursePO.builder()
                .subjectId(subjectId)
                .courseId(courseId)
                .build();
        //检查关系是否存在
        if(subjectCourseMapper.selectOne(
                Wrappers.<SubjectCoursePO>lambdaQuery()
                .eq(SubjectCoursePO::getSubjectId, subjectId)
                .eq(SubjectCoursePO::getCourseId, courseId)) != null){
            return Map.of("message","关系已存在");
        }
        else {
            if (subjectCourseMapper.insert(subjectCoursePO) > 0) {
                return Map.of("message", "添加关系成功");
            } else {
                return Map.of("message", "添加关系失败");
            }
        }
    }

    @Override
    public List<CourseBO> getCoursesBySubjectId(Long subjectId) {
        List<Long> courseIds = subjectCourseMapper.selectList(
                        Wrappers.<SubjectCoursePO>lambdaQuery()
                                .select(SubjectCoursePO::getCourseId)
                                .eq(SubjectCoursePO::getSubjectId, subjectId)
                ).stream()
                .map(SubjectCoursePO::getCourseId)
                .collect(Collectors.toList());

        // 2. 直接根据course_id字段查询课程
        List<CoursePO> courses = courseMapper.selectList(
                Wrappers.<CoursePO>lambdaQuery()
                        .in(CoursePO::getId, courseIds)
        );

        // 3. 转换为业务对象（解决空值和类型问题）
        return courses.stream()
                .map(course -> {
                    CourseBO courseBO = new CourseBO();
                    courseBO.setId(course.getId());
                    courseBO.setName(course.getName());
                    courseBO.setCode(course.getCode());
                    courseBO.setDescription(course.getDescription());
                    courseBO.setCredit(course.getCredit());
                    courseBO.setCategory(course.getCategory());
                    courseBO.setCreateTime(course.getCreateTime());
                    courseBO.setUpdateTime(course.getUpdateTime());
                    courseBO.setStatus(course.getStatus());

                    return courseBO;

                })
                .collect(Collectors.toList());
    }



    @Override
    public SubjectBO getSubjectByCourseId(Long courseId) {

         SubjectCoursePO subjectCoursePO = subjectCourseMapper.selectOne(
                 Wrappers.<SubjectCoursePO>lambdaQuery()
                 .eq(SubjectCoursePO::getCourseId, courseId));

         if(subjectCoursePO == null){
             return null;
         }
         return SubjectBO.fromSubjectPO(subjectMapper.selectById(subjectCoursePO.getSubjectId()));
    }

    @Override
    public Map<String, Object> deleteSubjectCourseRelation(Long subjectId, Long courseId) {

        if(subjectCourseMapper.delete(Wrappers.<SubjectCoursePO>lambdaQuery().eq(SubjectCoursePO::getSubjectId, subjectId).eq(SubjectCoursePO::getCourseId, courseId))>0){
            return Map.of("message","删除关系成功");
        }
        else{
            return Map.of("message","删除关系失败");
        }
    }

    @Override
    public List<SubjectBO> getAllSubjects() {
        return subjectMapper.selectList(null).stream().map(SubjectBO::fromSubjectPO).toList();
    }
}
