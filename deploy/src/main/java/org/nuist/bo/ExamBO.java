package org.nuist.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.ExamPo;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamBO {

    private Long examId;
    private String title;
    private String description;
    private Long courseId;
    private Long teacherId;
    private Long totalScore;
    private Integer durationMinutes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ExamBO fromExam(ExamPo examPo) {
        return ExamBO.builder()
                .examId(examPo.getExamId())
                .title(examPo.getTitle())
                .description(examPo.getDescription())
                .courseId(examPo.getCourseId())
                .teacherId(examPo.getTeacherId())
                .totalScore(examPo.getTotalScore())
                .durationMinutes(examPo.getDurationMinutes())
                .startTime(examPo.getStartTime())
                .endTime(examPo.getEndTime())
                .status(examPo.getStatus())
                .createdAt(examPo.getCreatedAt())
                .updatedAt(examPo.getUpdatedAt())
                .build();
    }

    public ExamPo toExam() {
        ExamPo examPo = new ExamPo();
        examPo.setExamId(examId);
        examPo.setTitle(title);
        examPo.setDescription(description);
        examPo.setCourseId(courseId);
        examPo.setTeacherId(teacherId);
        examPo.setTotalScore(totalScore);
        examPo.setDurationMinutes(durationMinutes);
        examPo.setStartTime(startTime);
        examPo.setEndTime(endTime);
        examPo.setStatus(status);
        examPo.setCreatedAt(createdAt);
        examPo.setUpdatedAt(updatedAt);
        return examPo;
    }

}
