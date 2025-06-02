package org.nuist.dto;

import lombok.Data;

@Data
public class UpdateKnowledgeDTO {

    private Long knowledgeId;
    private String name;
    private String description;
    private Long teacherId;
    private String difficultyLevel;
    private String teachPlan;

}
