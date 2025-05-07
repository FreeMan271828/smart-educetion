package org.nuist.business_object;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.model.Knowledge;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeBO {
    private int knowledgeId;
    private String name;
    private String description;
    private String difficultyLevel;
    private Long teacherId;
    private Long courseId;
    private String teachPlan;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static KnowledgeBO fromKnowledge(Knowledge knowledge) {
        return KnowledgeBO.builder()
                .knowledgeId(knowledge.getKnowledgeId())
                .name(knowledge.getName())
                .description(knowledge.getDescription())
                .difficultyLevel(knowledge.getDifficultyLevel())
                .teacherId(knowledge.getTeacherId())
                .courseId(knowledge.getCourseId())
                .teachPlan(knowledge.getTeachPlan())
                .createdAt(knowledge.getCreatedAt())
                .updatedAt(knowledge.getUpdatedAt())
                .build();
    }

    public Knowledge toKnowledge() {
        Knowledge knowledge = new Knowledge();
        knowledge.setKnowledgeId(knowledgeId);
        knowledge.setName(name);
        knowledge.setDescription(description);
        knowledge.setDifficultyLevel(difficultyLevel);
        knowledge.setTeacherId(teacherId);
        knowledge.setCourseId(courseId);
        knowledge.setTeachPlan(teachPlan);
        knowledge.setCreatedAt(createdAt);
        knowledge.setUpdatedAt(updatedAt);
        return knowledge;
    }
}