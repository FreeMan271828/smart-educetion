package org.nuist.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.CodeQuestionPO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeQuestionBO {

    private Long id;
    private Long examId;
    private String title;
    private String description;
    private List<String> sampleInputs;
    private List<String> sampleOutputs;
    private List<String> caseInputs;
    private List<String> caseOutputs;
    private String referenceAnswer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CodeQuestionBO fromPO(CodeQuestionPO po) {
        return CodeQuestionBO.builder()
                .id(po.getId())
                .title(po.getTitle())
                .description(po.getDescription())
                .sampleInputs(po.getSampleInputs())
                .sampleOutputs(po.getSampleOutputs())
                .caseInputs(po.getCaseInputs())
                .caseOutputs(po.getCaseOutputs())
                .referenceAnswer(po.getReferenceAnswer())
                .examId(po.getExamId())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }

    public CodeQuestionPO toPO() {
        CodeQuestionPO po = new CodeQuestionPO();
        po.setId(id);
        po.setExamId(examId);
        po.setTitle(title);
        po.setDescription(description);
        po.setSampleInputs(sampleInputs);
        po.setSampleOutputs(sampleOutputs);
        po.setCaseInputs(caseInputs);
        po.setCaseOutputs(caseOutputs);
        po.setReferenceAnswer(referenceAnswer);
        po.setCreatedAt(createdAt);
        po.setUpdatedAt(updatedAt);
        return po;
    }
}
