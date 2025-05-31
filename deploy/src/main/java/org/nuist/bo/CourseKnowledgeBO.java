package org.nuist.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.CourseKnowledge;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseKnowledgeBO {
    private Long id;
    private Long courseId;
    private Long knowledgeId;
    private Integer sequenceNumber;
    private LocalDateTime createdAt;

    public static CourseKnowledgeBO fromCourseKnowledge(CourseKnowledge courseKnowledge) {
        return CourseKnowledgeBO.builder()
                .id(courseKnowledge.getId())
                .courseId(courseKnowledge.getCourseId())
                .knowledgeId(courseKnowledge.getKnowledgeId())
                .sequenceNumber(courseKnowledge.getSequenceNumber())
                .createdAt(courseKnowledge.getCreatedAt())
                .build();
    }

    public CourseKnowledge toCourseKnowledge() {
        CourseKnowledge courseKnowledge = new CourseKnowledge();
        courseKnowledge.setId(id);
        courseKnowledge.setCourseId(courseId);
        courseKnowledge.setKnowledgeId(knowledgeId);
        courseKnowledge.setSequenceNumber(sequenceNumber);
        courseKnowledge.setCreatedAt(createdAt);
        return courseKnowledge;
    }
}