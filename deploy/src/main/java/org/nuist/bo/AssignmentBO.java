package org.nuist.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.AssignmentPO;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentBO {

    @Schema(description = "作业ID")
    private Long assignmentId;

    @Schema(description = "作业类型（学生上传:STUDENT_UPLOAD/教师布置:TEACHER_ASSIGNED）")
    private String type;

    @Schema(description = "创建者ID")
    private Long creatorId;

    @Schema(description = "所属课程ID")
    private Long courseId;

    @Schema(description = "作业标题")
    private String title;

    @Schema(description = "作业描述")
    private String description;

    @Schema(description = "是否公开答案解析")
    private Boolean isAnswerPublic;

    @Schema(description = "是否允许查看分数")
    private Boolean isScoreVisible;

    @Schema(description = "是否允许重做")
    private Boolean isRedoAllowed;

    @Schema(description = "最大重做次数")
    private Integer maxAttempts;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "截止时间")
    private LocalDateTime endTime;

    @Schema(description = "作业状态（草稿:DRAFT/已发布:PUBLISHED/已结束:ENDED）")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    /**
     * 从持久化对象转换为业务对象
     * @param assignmentPo 持久化对象
     * @return 业务对象
     */
    public static AssignmentBO fromAssignment(AssignmentPO assignmentPo) {
        return AssignmentBO.builder()
                .assignmentId(assignmentPo.getAssignmentId())
                .type(assignmentPo.getType())
                .creatorId(assignmentPo.getCreatorId())
                .courseId(assignmentPo.getCourseId())
                .title(assignmentPo.getTitle())
                .description(assignmentPo.getDescription())
                .isAnswerPublic(assignmentPo.getIsAnswerPublic())
                .isScoreVisible(assignmentPo.getIsScoreVisible())
                .isRedoAllowed(assignmentPo.getIsRedoAllowed())
                .maxAttempts(assignmentPo.getMaxAttempts())
                .startTime(assignmentPo.getStartTime())
                .endTime(assignmentPo.getEndTime())
                .status(assignmentPo.getStatus())
                .createdAt(assignmentPo.getCreatedAt())
                .updatedAt(assignmentPo.getUpdatedAt())
                .build();
    }

    /**
     * 转换为持久化对象
     * @return 持久化对象
     */
    public AssignmentPO toAssignment() {
        AssignmentPO assignmentPo = new AssignmentPO();
        assignmentPo.setAssignmentId(assignmentId);
        assignmentPo.setType(type);
        assignmentPo.setCreatorId(creatorId);
        assignmentPo.setCourseId(courseId);
        assignmentPo.setTitle(title);
        assignmentPo.setDescription(description);
        assignmentPo.setIsAnswerPublic(isAnswerPublic);
        assignmentPo.setIsScoreVisible(isScoreVisible);
        assignmentPo.setIsRedoAllowed(isRedoAllowed);
        assignmentPo.setMaxAttempts(maxAttempts);
        assignmentPo.setStartTime(startTime);
        assignmentPo.setEndTime(endTime);
        assignmentPo.setStatus(status);
        assignmentPo.setCreatedAt(createdAt);
        assignmentPo.setUpdatedAt(updatedAt);
        return assignmentPo;
    }
}
