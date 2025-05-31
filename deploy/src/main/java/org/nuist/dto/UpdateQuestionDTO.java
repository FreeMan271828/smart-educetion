package org.nuist.dto;

import lombok.Data;

@Data
public class UpdateQuestionDTO {
    private Long questionId;

    private String content;
    private String questionType;
    private String difficulty;
    private String referenceAnswer;
    private Integer scorePoints;
    private String answer;
}
