package org.nuist.dto;

import lombok.Data;

@Data
public class AddQuestionDTO {
    private Long teacherId;
    private String content;
    private String questionType;
    private String difficulty;
    private Long knowledgeId;
    private String referenceAnswer;
    private Integer scorePoints;
    private String answer;
}
