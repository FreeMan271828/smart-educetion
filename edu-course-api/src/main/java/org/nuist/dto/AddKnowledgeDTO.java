package org.nuist.dto;

import lombok.Data;

@Data
public class AddKnowledgeDTO {

    private String name;
    private String description;
    private String difficultyLevel;
    private Long teacherId;
    private Long courseId;
    private String teachPlan;

}
