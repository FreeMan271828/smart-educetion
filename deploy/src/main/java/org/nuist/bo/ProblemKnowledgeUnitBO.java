package org.nuist.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.ProblemKnowledgeUnitPO;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProblemKnowledgeUnitBO {
    private Long id;

    private Long problemId;

    private Long knowledgeUnitId;

    @Schema(description = "权重,规范为1-5.5:核心知识点；3-4:解题必须但非核心；1-2:背景知识点")
    private Integer weight;

    private LocalDateTime createdAt;

    public static ProblemKnowledgeUnitBO fromPO(ProblemKnowledgeUnitPO po) {
        return ProblemKnowledgeUnitBO.builder()
                .id(po.getId())
                .problemId(po.getProblemId())
                .knowledgeUnitId(po.getKnowledgeUnitId())
                .weight(po.getWeight())
                .createdAt(po.getCreatedAt())
                .build();
    }

    public static ProblemKnowledgeUnitPO toPO(ProblemKnowledgeUnitBO bo) {
        return ProblemKnowledgeUnitPO.builder()
                .id(bo.getId())
                .problemId(bo.getProblemId())
                .knowledgeUnitId(bo.getKnowledgeUnitId())
                .weight(bo.getWeight())
                .createdAt(bo.getCreatedAt())
                .build();
    }
}
