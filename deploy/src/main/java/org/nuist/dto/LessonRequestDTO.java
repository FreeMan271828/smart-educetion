package org.nuist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequestDTO {
    private String subjectType;
    private String courseOutline;
    private Integer duration;
    private String difficultyLevel;
    private String teachingStyle;
} 