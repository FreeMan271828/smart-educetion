package org.nuist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.ProblemBankPO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemBankDTO {



    private String title;

    private String content;

    private String type;

    private String expectedAnswer;

    private Double score;


    public ProblemBankPO toProblemBankPO() {
        return ProblemBankPO.builder()
                .title(title)
                .content(content)
                .type(type)
                .expectedAnswer(expectedAnswer)
                .score(score)
                .build();
    }


}
