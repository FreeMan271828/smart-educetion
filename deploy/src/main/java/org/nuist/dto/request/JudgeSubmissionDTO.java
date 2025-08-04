package org.nuist.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class JudgeSubmissionDTO {
    private String language;
    private String code;
    private List<String> inputs;
    private List<String> expectedOutputs;
}
