package org.nuist.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.ProblemBankPO;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProblemBankBO {
    private Long id;

    private String origin;

    private String title;

    private String content;

    private String type;

    private String expectedAnswer;

    private Double score;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ProblemBankPO toProblemBankPO(ProblemBankBO problemBankBo) {
        return ProblemBankPO.builder()
                .id(problemBankBo.getId())
                .origin(problemBankBo.getOrigin())
                .title(problemBankBo.getTitle())
                .content(problemBankBo.getContent())
                .type(problemBankBo.getType())
                .expectedAnswer(problemBankBo.getExpectedAnswer())
                .score(problemBankBo.getScore())
                .createdAt(problemBankBo.getCreatedAt())
                .updatedAt(problemBankBo.getUpdatedAt())
                .build();
    }

    public static ProblemBankBO fromProblemBankPO(ProblemBankPO problemBankPo) {
        return ProblemBankBO.builder()
                .id(problemBankPo.getId())
                .origin(problemBankPo.getOrigin())
                .title(problemBankPo.getTitle())
                .content(problemBankPo.getContent())
                .type(problemBankPo.getType())
                .expectedAnswer(problemBankPo.getExpectedAnswer())
                .score(problemBankPo.getScore())
                .createdAt(problemBankPo.getCreatedAt())
                .updatedAt(problemBankPo.getUpdatedAt())
                .build();
    }
}
