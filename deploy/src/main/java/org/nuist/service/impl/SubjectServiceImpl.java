package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nuist.bo.SubjectBO;
import org.nuist.mapper.SubjectCourseMapper;
import org.nuist.mapper.SubjectMapper;
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
        if(subjectCourseMapper.insert(subjectCoursePO)>0){
            return Map.of("message","添加关系成功");
        }
        else{
            return Map.of("message","添加关系失败");
        }
    }

    @Override
    public List<SubjectBO> getCoursesBySubjectId(Long subjectId) {
        List<Long> courseIds = subjectCourseMapper.selectList(Wrappers.<SubjectCoursePO>lambdaQuery().eq(SubjectCoursePO::getSubjectId, subjectId)).stream().map(SubjectCoursePO::getCourseId).toList();
        return courseIds.stream().map(courseId -> SubjectBO.fromSubjectPO(subjectMapper.selectById(courseId))).toList();
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
}
