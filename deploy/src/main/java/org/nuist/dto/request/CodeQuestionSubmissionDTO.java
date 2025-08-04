package org.nuist.dto.request;

import lombok.Data;

@Data
public class CodeQuestionSubmissionDTO {
    private Long codeQuestionId;
    private Long studentId;
    private String language;
    private String studentCode;
}
