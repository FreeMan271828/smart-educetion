package org.nuist.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.CodeQuestionAnswerPO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeQuestionAnswerBO {

    private Long id;
    private Long codeQuestionId;
    private Long studentId;
    private String language;
    private String studentCode;
    private Integer caseAccepted;
    private Integer caseTotal;
    private Double score;
    private String status;
    private Long timeMs;
    private LocalDateTime submitTime;

    public static CodeQuestionAnswerBO fromPO(CodeQuestionAnswerPO po) {
        return CodeQuestionAnswerBO.builder()
                .id(po.getId())
                .codeQuestionId(po.getCodeQuestionId())
                .studentId(po.getStudentId())
                .language(po.getLanguage())
                .studentCode(po.getStudentCode())
                .caseAccepted(po.getCaseAccepted())
                .caseTotal(po.getCaseTotal())
                .score(po.getScore())
                .status(po.getStatus())
                .timeMs(po.getTimeMs())
                .submitTime(po.getSubmitTime())
                .build();
    }

    public CodeQuestionAnswerPO toPO() {
        CodeQuestionAnswerPO po = new CodeQuestionAnswerPO();
        po.setId(this.id);
        po.setCodeQuestionId(this.codeQuestionId);
        po.setStudentId(this.studentId);
        po.setLanguage(this.language);
        po.setStudentCode(this.studentCode);
        po.setCaseAccepted(this.caseAccepted);
        po.setCaseTotal(this.caseTotal);
        po.setScore(this.score);
        po.setStatus(this.status);
        po.setTimeMs(this.timeMs);
        po.setSubmitTime(this.submitTime);
        return po;
    }
}
