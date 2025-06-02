package org.nuist.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeBO {
    private Long knowledgeId;
    private String name;
    private String description;
    private String difficultyLevel;
    private Long teacherId;
    private String teachPlan;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static KnowledgeBO fromKnowledge(org.nuist.po.Knowledge knowledge) {
        return KnowledgeBO.builder()
                .knowledgeId(knowledge.getKnowledgeId())
                .name(knowledge.getName())
                .description(knowledge.getDescription())
                .difficultyLevel(knowledge.getDifficultyLevel())
                .teacherId(knowledge.getTeacherId())
                .teachPlan(knowledge.getTeachPlan())
                .createdAt(knowledge.getCreatedAt())
                .updatedAt(knowledge.getUpdatedAt())
                .build();
    }

    public org.nuist.po.Knowledge toKnowledge() {
        org.nuist.po.Knowledge knowledge = new org.nuist.po.Knowledge();
        knowledge.setKnowledgeId(knowledgeId);
        knowledge.setName(name);
        knowledge.setDescription(description);
        knowledge.setDifficultyLevel(difficultyLevel);
        knowledge.setTeacherId(teacherId);
        knowledge.setTeachPlan(teachPlan);
        knowledge.setCreatedAt(createdAt);
        knowledge.setUpdatedAt(updatedAt);
        return knowledge;
    }
}