package org.nuist.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.nuist.handlers.PostgresEnumTypeHandler;

import java.time.LocalDateTime;

@TableName("student_problem_answer")
@Data
public class StudentProblemAnswerPO {




    @TableId(type = IdType.AUTO)
    private Long answerId;

    @TableField("student_id")
    private Long studentId;

    @TableField("assignment_id")
    private Long assignmentId;

    @TableField("problem_id")
    private Long problemId;

    @TableField("answer")
    private String answer;

    @TableField("is_auto_graded")
    private Boolean isAutoGraded;

    @TableField("auto_score")
    private Double autoScore;

    @TableField("manual_score")
    private Double manualScore;

    @TableField("attempt_number")
    private Integer attemptNumber;

    @TableField(value = "status", typeHandler = PostgresEnumTypeHandler.class)
    private String status;

    @TableField(value = "grading_status", typeHandler = PostgresEnumTypeHandler.class)
    private String gradingStatus;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;


    @TableField(value = "final_score", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private Double finalScore;
}