package org.nuist.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddExamDTO {
    private String title;
    private String description;
    private Long courseId;
    private Long teacherId;
    private Integer totalScore;
    private Integer durationMinutes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;  // TODO 是否可以改成枚举
}
