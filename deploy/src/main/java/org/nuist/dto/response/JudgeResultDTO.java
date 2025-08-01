package org.nuist.dto.response;

import lombok.Data;

@Data
public class JudgeResultDTO {
    private String status;
    private String stdout;
    private String stderr;
    private Long timeMs;
}
