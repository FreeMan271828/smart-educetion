package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.nuist.business_object.ExamBO;
import org.nuist.dto.AddExamDTO;
import org.nuist.dto.UpdateExamDTO;
import org.nuist.mapper.ExamMapper;
import org.nuist.model.Exam;
import org.nuist.service.ExamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    private final ExamMapper examMapper;

    @Override
    public ExamBO getExamById(Long id) {
        if (id == null) {
            return null;
        }
        Exam exam = examMapper.selectById(id);
        if (exam == null) {
            return null;
        }
        return ExamBO.fromExam(exam);
    }

    @Override
    public List<ExamBO> getExamsByCourseId(Long courseId) {
        if (courseId == null) {
            return new ArrayList<>();
        }
        return convertToExamBO(
                list(Wrappers.<Exam>lambdaQuery()
                        .eq(Exam::getCourseId, courseId)));
    }

    @Override
    public List<ExamBO> getExamsByTeacherId(Long teacherId) {
        if (teacherId == null) {
            return new ArrayList<>();
        }
        return convertToExamBO(
                list(Wrappers.<Exam>lambdaQuery()
                        .eq(Exam::getTeacherId, teacherId)));
    }

    @Override
    public List<ExamBO> getExamsByTeacherInCourse(Long courseId, Long teacherId) {
        if (courseId == null || teacherId == null) {
            return new ArrayList<>();
        }
        return convertToExamBO(
                list(Wrappers.<Exam>lambdaQuery()
                        .eq(Exam::getCourseId, courseId)
                        .eq(Exam::getTeacherId, teacherId))
        );
    }

    @Override
    public ExamBO saveExam(AddExamDTO addExamDTO) {
        Exam persistedExam = new Exam();
        persistedExam.setTitle(addExamDTO.getTitle());
        persistedExam.setDescription(addExamDTO.getDescription());
        persistedExam.setCourseId(addExamDTO.getCourseId());
        persistedExam.setTeacherId(addExamDTO.getTeacherId());
        persistedExam.setTotalScore(addExamDTO.getTotalScore());
        persistedExam.setDurationMinutes(addExamDTO.getDurationMinutes());
        persistedExam.setStartTime(addExamDTO.getStartTime());
        persistedExam.setEndTime(addExamDTO.getEndTime());
        persistedExam.setStatus(addExamDTO.getStatus());

        examMapper.insert(persistedExam);
        return ExamBO.fromExam(persistedExam);
    }

    @Override
    public ExamBO updateExam(UpdateExamDTO updateExamDTO) {
        // DTO中，id为目标考试标识，剩余字段为新值，若为null则不更新
        Exam exam = examMapper.selectById(updateExamDTO.getExamId());
        if (exam == null) {
            throw new IllegalArgumentException("Exam ID " + updateExamDTO.getExamId() + " not found");
        }

        if (updateExamDTO.getTotalScore() != null) {
            exam.setTotalScore(updateExamDTO.getTotalScore());
        }
        if (updateExamDTO.getDurationMinutes() != null) {
            exam.setDurationMinutes(updateExamDTO.getDurationMinutes());
        }
        if (updateExamDTO.getStartTime() != null) {
            exam.setStartTime(updateExamDTO.getStartTime());
        }
        if (updateExamDTO.getEndTime() != null) {
            exam.setEndTime(updateExamDTO.getEndTime());
        }
        if (updateExamDTO.getStatus() != null) {
            exam.setStatus(updateExamDTO.getStatus());
        }

        examMapper.updateById(exam);
        return ExamBO.fromExam(exam);
    }

    @Override
    public boolean deleteExam(Long id) {
        return examMapper.deleteById(id) > 0;
    }

    private List<ExamBO> convertToExamBO(List<Exam> exams) {
        return exams.stream().map(ExamBO::fromExam).collect(Collectors.toList());
    }
}
