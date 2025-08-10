package org.nuist.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.SubjectPO;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectBO {
    private Long id;

    private String name;

    private String description;

    private String grade;

    private Integer status;

    private String category;

    private Integer credit;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static SubjectBO fromSubjectPO(SubjectPO subjectPO) {
        return SubjectBO.builder()
                .id(subjectPO.getId())
                .name(subjectPO.getName())
                .description(subjectPO.getDescription())
                .grade(subjectPO.getGrade())
                .status(subjectPO.getStatus())
                .category(subjectPO.getCategory())
                .credit(subjectPO.getCredit())
                .createdAt(subjectPO.getCreatedAt())
                .updatedAt(subjectPO.getUpdatedAt())
                .build();
    }

    public static SubjectPO toSubjectPO(SubjectBO subjectBO) {
        return SubjectPO.builder()
                .id(subjectBO.getId())
                .name(subjectBO.getName())
                .description(subjectBO.getDescription())
                .grade(subjectBO.getGrade())
                .status(subjectBO.getStatus())
                .category(subjectBO.getCategory())
                .credit(subjectBO.getCredit())
                .createdAt(subjectBO.getCreatedAt())
                .updatedAt(subjectBO.getUpdatedAt())
                .build();
    }
}
