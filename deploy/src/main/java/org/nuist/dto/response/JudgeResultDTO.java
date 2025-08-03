package org.nuist.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JudgeResultDTO {
    private String status;
    private List<String> statusPerCase = new ArrayList<>();
    private List<String> stdout = new ArrayList<>();
    private List<String> stderr = new ArrayList<>();
    private Long timeMs;
}
