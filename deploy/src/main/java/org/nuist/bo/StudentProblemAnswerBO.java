package org.nuist.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.StudentProblemAnswerPO;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProblemAnswerBO {

    @Schema(description = "答案ID")
    private Long answerId;

    @Schema(description = "学生ID")
    private Long studentId;

    @Schema(description = "作业ID")
    private Long assignmentId;

    @Schema(description = "题目ID")
    private Long problemId;

    @Schema(description = "学生答案")
    private String answer;

    @Schema(description = "是否自动批改（仅对选填判断生效）")
    private Boolean isAutoGraded;

    @Schema(description = "自动批改得分")
    private Double autoScore;

    @Schema(description = "人工批改得分")
    private Double manualScore;

    @Schema(description = "尝试次数，初次作答时默认为1")
    private Integer attemptNumber;

    @Schema(description = "回答状态 (NOT_SUBMITTED/未提交, SUBMITTED/已提交, SAVED/已保存)")
    private String status;

    @Schema(description = "批改状态 (PENDING/待批改, GRADED/已批改, REVIEWED/已复核)初次作答默认PENDING")
    private String gradingStatus;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @Schema(description = "最终得分,由数据库自动计算，优先取人工批改得分")
    private Double finalScore;

    /**
     * 从持久化对象转换为业务对象
     * @param po 持久化对象
     * @return 业务对象
     */
    public static StudentProblemAnswerBO fromPO(StudentProblemAnswerPO po) {
        return StudentProblemAnswerBO.builder()
                .answerId(po.getAnswerId())
                .studentId(po.getStudentId())
                .assignmentId(po.getAssignmentId())
                .problemId(po.getProblemId())
                .answer(po.getAnswer())
                .isAutoGraded(po.getIsAutoGraded())
                .autoScore(po.getAutoScore())
                .manualScore(po.getManualScore())
                .attemptNumber(po.getAttemptNumber())
                .status(po.getStatus())
                .gradingStatus(po.getGradingStatus())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .finalScore(po.getFinalScore())
                .build();
    }

    /**
     * 转换为持久化对象
     * @return 持久化对象
     */
    public StudentProblemAnswerPO toPO() {
        StudentProblemAnswerPO po = new StudentProblemAnswerPO();
        po.setAnswerId(answerId);
        po.setStudentId(studentId);
        po.setAssignmentId(assignmentId);
        po.setProblemId(problemId);
        po.setAnswer(answer);
        po.setIsAutoGraded(isAutoGraded);
        po.setAutoScore(autoScore);
        po.setManualScore(manualScore);
        po.setAttemptNumber(attemptNumber);
        po.setStatus(status);
        po.setGradingStatus(gradingStatus);
        po.setCreatedAt(createdAt);
        po.setUpdatedAt(updatedAt);
        // 注意：final_score 是计算字段，通常不需要设置
        return po;
    }
}