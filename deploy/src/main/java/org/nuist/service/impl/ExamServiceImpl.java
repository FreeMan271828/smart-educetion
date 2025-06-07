package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.nuist.bo.ExamBO;
import org.nuist.mapper.ExamMapper;
import org.nuist.po.ExamPo;
import org.nuist.service.ExamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamMapper examMapper;

    @Override
    public ExamBO getExamById(Long id) {
        if (id == null) {
            return null;
        }
        ExamPo examPo = examMapper.selectById(id);
        if (examPo == null) {
            return null;
        }
        return ExamBO.fromExam(examPo);
    }

    @Override
    public List<ExamBO> getExamsByCourseId(Long courseId) {
        if (courseId == null) {
            return new ArrayList<>();
        }
        List<ExamPo> examPos = examMapper.selectList(Wrappers.<ExamPo>lambdaQuery()
                .eq(ExamPo::getCourseId, courseId));
        return convertToExamBO(examPos);
    }

    @Override
    public List<ExamBO> getExamsByTeacherId(Long teacherId) {
        if (teacherId == null) {
            return new ArrayList<>();
        }
        List<ExamPo> examPos = examMapper.selectList(Wrappers.<ExamPo>lambdaQuery()
                .eq(ExamPo::getTeacherId, teacherId));
        return convertToExamBO(examPos);
    }

    @Override
    public List<ExamBO> getExamsByTeacherInCourse(Long courseId, Long teacherId) {
        if (courseId == null || teacherId == null) {
            return new ArrayList<>();
        }
        List<ExamPo> examPos = examMapper.selectList(Wrappers.<ExamPo>lambdaQuery()
                .eq(ExamPo::getCourseId, courseId)
                .eq(ExamPo::getTeacherId, teacherId));
        return convertToExamBO(examPos);
    }

    @Override
    public List<ExamBO> getExamsInCourseByType(Long courseId, String type) {
        if (courseId == null || !StringUtils.hasText(type)) {
            return new ArrayList<>();
        }
        return convertToExamBO(
                examMapper.selectList(
                        Wrappers.<ExamPo>lambdaQuery()
                                .eq(ExamPo::getCourseId, courseId)
                                .eq(ExamPo::getType, type)
                )
        );
    }

    @Override
    public ExamBO saveExam(ExamBO examBo) {
        ExamPo persistedExamPo = examBo.toExam();

        examMapper.insert(persistedExamPo);
        return ExamBO.fromExam(persistedExamPo);
    }

    @Override
    public ExamBO updateExam(ExamBO examBo) {
        // DTO中，id为目标考试标识，剩余字段为新值，若为null则不更新
        ExamPo examPo = examMapper.selectById(examBo.getExamId());
        if (examPo == null) {
            throw new IllegalArgumentException("ExamPo ID " + examBo.getExamId() + " not found");
        }

        if (examBo.getTotalScore() != null) {
            examPo.setTotalScore(examBo.getTotalScore());
        }
        if (examBo.getDurationMinutes() != null) {
            examPo.setDurationMinutes(examBo.getDurationMinutes());
        }
        if (examBo.getStartTime() != null) {
            examPo.setStartTime(examBo.getStartTime());
        }
        if (examBo.getEndTime() != null) {
            examPo.setEndTime(examBo.getEndTime());
        }
        if (examBo.getStatus() != null) {
            examPo.setStatus(examBo.getStatus());
        }

        examMapper.updateById(examPo);
        return ExamBO.fromExam(examPo);
    }

    @Override
    public boolean deleteExam(Long id) {
        return examMapper.deleteById(id) > 0;
    }

    private List<ExamBO> convertToExamBO(List<ExamPo> examPos) {
        return examPos.stream().map(ExamBO::fromExam).collect(Collectors.toList());
    }
}
