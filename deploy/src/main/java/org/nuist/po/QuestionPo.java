package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("question")
@Data
public class QuestionPo {

    @TableId(type = IdType.AUTO)
    private Long questionId;

    @TableField("teacher_id")
    private Long teacherId;

    @TableField("content")
    private String content;

    @TableField("question_type")
    private String questionType;

    @TableField("difficulty")
    private String difficulty;

    @TableField("knowledge_id")
    private Long knowledgeId;

    @TableField("exam_id")
    private Long examId;

    @TableField("reference_answer")
    private String referenceAnswer;

    @TableField("score_points")
    private Long scorePoints;

    @TableField("answer")
    private String answer;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
