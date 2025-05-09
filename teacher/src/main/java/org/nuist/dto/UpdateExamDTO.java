package org.nuist.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateExamDTO {
    private Long examId;

    private String title;
    private String description;
    private Integer totalScore;
    private Integer durationMinutes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
