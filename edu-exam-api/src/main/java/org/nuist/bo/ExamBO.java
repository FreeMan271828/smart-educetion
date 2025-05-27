package org.nuist.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.Exam;

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
    private Integer totalScore;
    private Integer durationMinutes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ExamBO fromExam(Exam exam) {
        return ExamBO.builder()
                .examId(exam.getExamId())
                .title(exam.getTitle())
                .description(exam.getDescription())
                .courseId(exam.getCourseId())
                .teacherId(exam.getTeacherId())
                .totalScore(exam.getTotalScore())
                .durationMinutes(exam.getDurationMinutes())
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .status(exam.getStatus())
                .createdAt(exam.getCreatedAt())
                .updatedAt(exam.getUpdatedAt())
                .build();
    }

    public Exam toExam() {
        Exam exam = new Exam();
        exam.setExamId(examId);
        exam.setTitle(title);
        exam.setDescription(description);
        exam.setCourseId(courseId);
        exam.setTeacherId(teacherId);
        exam.setTotalScore(totalScore);
        exam.setDurationMinutes(durationMinutes);
        exam.setStartTime(startTime);
        exam.setEndTime(endTime);
        exam.setStatus(status);
        exam.setCreatedAt(createdAt);
        exam.setUpdatedAt(updatedAt);
        return exam;
    }

}
