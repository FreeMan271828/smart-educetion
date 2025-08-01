package org.nuist.dto.request;

import lombok.Data;

@Data
public class JudgeSubmissionDTO {
    private String language;
    private String code;
    private String input;
    private String expectedOutput;
}
