package org.nuist.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.ProblemPO;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemBO {

    @Schema(description = "题目ID")
    private Long problemId;

    @Schema(description = "所属作业ID")
    private Long assignmentId;

    @Schema(description = "题目来源类型（学生上传:STUDENT_UPLOAD/教师布置:TEACHER_ASSIGNED）")
    private String originType;

    @Schema(description = "题目标题")
    private String title;

    @Schema(description = "题目内容（题干）")
    private String content;

    @Schema(description = "题目类型（单选题:SINGLE_CHOICE/多选题:MULTI_CHOICE/填空题:FILL_BLANK/简答题:ESSAY_QUESTION/判断题:TRUE_FALSE）")
    private String type;

    @Schema(description = "是否自动判分")
    private Boolean autoGrading;

    @Schema(description = "标准答案")
    private String expectedAnswer;

    @Schema(description = "题目分值")
    private Double score;

    @Schema(description = "题目序号")
    private Integer sequence;

    @Schema(description = "创建时间（默认为当前时间）")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间（默认为当前时间）")
    private LocalDateTime updatedAt;

    /**
     * 从持久化对象转换为业务对象
     * @param problemPo 持久化对象
     * @return 业务对象
     */
    public static ProblemBO fromProblem(ProblemPO problemPo) {
        return ProblemBO.builder()
                .problemId(problemPo.getProblemId())
                .assignmentId(problemPo.getAssignmentId())
                .originType(problemPo.getOriginType())
                .title(problemPo.getTitle())
                .content(problemPo.getContent())
                .type(problemPo.getType())
                .autoGrading(problemPo.getAutoGrading())
                .expectedAnswer(problemPo.getExpectedAnswer())
                .score(problemPo.getScore())
                .sequence(problemPo.getSequence())
                .createdAt(problemPo.getCreatedAt())
                .updatedAt(problemPo.getUpdatedAt())
                .build();
    }

    /**
     * 转换为持久化对象
     * @return 持久化对象
     */
    public ProblemPO toProblem() {
        ProblemPO problemPo = new ProblemPO();
        problemPo.setProblemId(problemId);
        problemPo.setAssignmentId(assignmentId);
        problemPo.setOriginType(originType);
        problemPo.setTitle(title);
        problemPo.setContent(content);
        problemPo.setType(type);
        problemPo.setAutoGrading(autoGrading);
        problemPo.setExpectedAnswer(expectedAnswer);
        problemPo.setScore(score);
        problemPo.setSequence(sequence);
        problemPo.setCreatedAt(createdAt);
        problemPo.setUpdatedAt(updatedAt);
        return problemPo;
    }
}