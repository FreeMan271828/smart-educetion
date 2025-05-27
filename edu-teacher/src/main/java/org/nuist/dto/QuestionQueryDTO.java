package org.nuist.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class QuestionQueryDTO {

    private String questionType;
    private String difficulty;
    private LocalDate startTime;
    private LocalDate endTime;

}