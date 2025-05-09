package org.nuist.dto;

import lombok.Data;

@Data
public class UpdateKnowledgeDTO {

    private int knowledgeId;
    private String name;
    private String description;
    private String difficultyLevel;
    private String teachPlan;

}
