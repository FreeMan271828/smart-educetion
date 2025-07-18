package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nuist.bo.AssignmentBO;
import org.nuist.bo.CourseBO;
import org.nuist.bo.ProblemBO;
import org.nuist.bo.StudentBO;
import org.nuist.constant.AssignmentStatus;
import org.nuist.constant.AssignmentType;
import org.nuist.mapper.AssignmentMapper;
import org.nuist.po.AssignmentPO;
import org.nuist.service.AssignmentService;
import org.nuist.service.CourseSelectionService;
import org.nuist.service.ProblemService;
import org.nuist.service.StudentProblemAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    private CourseSelectionService courseSelectionService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private StudentProblemAnswerService studentProblemAnswerService;

    @Override
    public AssignmentBO getAssignmentById(Long assignmentId) {
        if (assignmentId == null) {
            return null;
        }
        AssignmentPO assignmentPo = assignmentMapper.selectById(assignmentId);
        if (assignmentPo == null) {
            return null;
        }
        return AssignmentBO.fromAssignment(assignmentPo);
    }

    @Override
    public List<AssignmentBO> getAssignmentsInCourseByType(Long courseId, String type) {
        if (courseId == null || !StringUtils.hasText(type)) {
            return Collections.emptyList();
        }

        // 使用自定义查询方法（解决枚举类型问题）
        List<AssignmentPO> assignments = assignmentMapper.findByCourseIdAndType(courseId, type);
        return convertToAssignmentBO(assignments);
    }

    @Override
    public List<AssignmentBO> getAssignmentsInCourseByCreatorIdCourseId(Long courseId, Long creatorId) {
        if (courseId == null || creatorId == null) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<AssignmentPO> query = Wrappers.lambdaQuery();
        query.eq(AssignmentPO::getCourseId, courseId)
                .eq(AssignmentPO::getCreatorId, creatorId);

        List<AssignmentPO> assignments = assignmentMapper.selectList(query);
        return convertToAssignmentBO(assignments);
    }

    @Override
    public AssignmentBO saveAssignment(AssignmentBO assignmentBO) {
        // 转换为持久化对象
        AssignmentPO assignmentPo = assignmentBO.toAssignment();

        // 插入数据库
        int result = assignmentMapper.insert(assignmentPo);
        if (result <= 0) {
            throw new RuntimeException("保存作业失败");
        }

        // 返回带ID的业务对象
        return AssignmentBO.fromAssignment(assignmentPo);
    }

    @Override
    public AssignmentBO updateAssignment(AssignmentBO assignmentBO) {
        // 检查ID是否存在
        if (assignmentBO.getAssignmentId() == null) {
            throw new IllegalArgumentException("作业ID不能为空");
        }

        // 获取现有作业
        AssignmentPO existing = assignmentMapper.selectById(assignmentBO.getAssignmentId());
        if (existing == null) {
            throw new IllegalArgumentException("作业不存在: " + assignmentBO.getAssignmentId());
        }

        // 更新可修改字段
        if (assignmentBO.getTitle() != null) {
            existing.setTitle(assignmentBO.getTitle());
        }
        if (assignmentBO.getDescription() != null) {
            existing.setDescription(assignmentBO.getDescription());
        }
        if (assignmentBO.getIsAnswerPublic() != null) {
            existing.setIsAnswerPublic(assignmentBO.getIsAnswerPublic());
        }
        if (assignmentBO.getIsScoreVisible() != null) {
            existing.setIsScoreVisible(assignmentBO.getIsScoreVisible());
        }
        if (assignmentBO.getIsRedoAllowed() != null) {
            existing.setIsRedoAllowed(assignmentBO.getIsRedoAllowed());
        }
        if (assignmentBO.getMaxAttempts() != null) {
            existing.setMaxAttempts(assignmentBO.getMaxAttempts());
        }
        if (assignmentBO.getStartTime() != null) {
            existing.setStartTime(assignmentBO.getStartTime());
        }
        if (assignmentBO.getEndTime() != null) {
            existing.setEndTime(assignmentBO.getEndTime());
        }
        if (assignmentBO.getStatus() != null) {
            existing.setStatus(assignmentBO.getStatus());
        }

        // 更新数据库
        int result = assignmentMapper.updateById(existing);
        if (result <= 0) {
            throw new RuntimeException("更新作业失败");
        }

        return AssignmentBO.fromAssignment(existing);
    }

    @Override
    public boolean deleteAssignment(Long assignmentId) {
        if (assignmentId == null) {
            return false;
        }
        return assignmentMapper.deleteById(assignmentId) > 0;
    }

    @Override
    public List<AssignmentBO> getAssignmentsInCourseByCreatorId(Long creatorId) {
        if (creatorId == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AssignmentPO> query = Wrappers.lambdaQuery();
        query.eq(AssignmentPO::getCreatorId, creatorId);
        List<AssignmentPO> assignments = assignmentMapper.selectList(query);
        return convertToAssignmentBO(assignments);

    }

    @Override
    public List<AssignmentBO> getAssignmentsInCourseByCreatorIdAndType(Long creatorId, String type) {
        if (creatorId == null || !StringUtils.hasText(type)) {
            return Collections.emptyList();
        }

        // 使用自定义查询方法（解决枚举类型问题）
        List<AssignmentPO> assignments = assignmentMapper.findByCreatorIdAndType(
                creatorId,
                type
        );

        return convertToAssignmentBO(assignments);
    }

    @Override
    public List<AssignmentBO> getIncompleteAssignments(Long studentId) {
        // 获取学生已选课程
        List<CourseBO> courses = courseSelectionService.getCourseSelections(studentId);
        // 获取每个课程的作业
        List<AssignmentBO> assignments = new ArrayList<>();
        for (CourseBO course : courses) {
            assignments.addAll(getAssignmentsInCourseByType(course.getId(), AssignmentType.TEACHER_ASSIGNED));
        }
        // 获取每个作业的完成度, 并判断是否完成
        List<AssignmentBO> incompleteAssignments = new ArrayList<>();
        for(AssignmentBO assignment : assignments){
            Map<String, Object> rate= studentProblemAnswerService.getCompletionRate(studentId, assignment.getAssignmentId());
            if(!isCompleted((Double) rate.get("completionRate"))){
                incompleteAssignments.add(assignment);
            }
        }
        return incompleteAssignments;
    }

    @Override
    public List<Long> getIncompleteStudentsByCourseIdAndAssignmentId(Long courseId, Long assignmentId) {
        List<StudentBO> students = courseSelectionService.getStudentsByCourseId(courseId);
        List<Long> incompleteStudentsId = new ArrayList<>();
        for(StudentBO student : students){
            Map<String, Object> rate= studentProblemAnswerService.getCompletionRate(student.getStudentId(), assignmentId);
            if(!isCompleted((Double) rate.get("completionRate"))){
                incompleteStudentsId.add(student.getStudentId());
            }
        }

        return incompleteStudentsId;
    }

    private boolean isCompleted(Double rate) {
        return rate != null && Math.abs(rate - 1.0) < 0.001;
    }

    /**
     * 将持久化对象列表转换为业务对象列表
     * @param assignments 持久化对象列表
     * @return 业务对象列表
     */
    private List<AssignmentBO> convertToAssignmentBO(List<AssignmentPO> assignments) {
        return assignments.stream()
                .map(AssignmentBO::fromAssignment)
                .collect(Collectors.toList());
    }
}
