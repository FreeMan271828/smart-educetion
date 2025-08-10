package org.nuist.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.KnowledgeUnitPO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KnowledgeUnitBO {
    private long id;

    private String name;

    private String subject;

    private LocalDateTime createdAt;

    @Schema(description = "状态（0:预置/1:ai待审/2：废弃）")
    private Integer status;

    public static KnowledgeUnitBO fromKnowledgeUnitPO(KnowledgeUnitPO knowledgeUnitPo) {
        return KnowledgeUnitBO.builder()
                .id(knowledgeUnitPo.getId())
                .name(knowledgeUnitPo.getName())
                .subject(knowledgeUnitPo.getSubject())
                .createdAt(knowledgeUnitPo.getCreatedAt())
                .status(knowledgeUnitPo.getStatus())
                .build();
    }

    public static KnowledgeUnitPO toKnowledgeUnitPO(KnowledgeUnitBO knowledgeUnitBo) {
        return KnowledgeUnitPO.builder()
                .name(knowledgeUnitBo.getName())
                .subject(knowledgeUnitBo.getSubject())
                .createdAt(knowledgeUnitBo.getCreatedAt())
                .status(knowledgeUnitBo.getStatus())
                .build();
    }

}
