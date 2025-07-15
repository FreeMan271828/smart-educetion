package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nuist.handlers.PostgresEnumTypeHandler;

import java.time.LocalDateTime;

@TableName("problem")
@Data
public class ProblemPO {
    @TableId(type = IdType.AUTO)
    private Long problemId;

    /**
     * 所属作业ID
     */
    @TableField("assignment_id")
    private Long assignmentId;

    /**
     * 题目来源类型（学生上传/教师布置）
     */
    @TableField(value = "origin_type", typeHandler = PostgresEnumTypeHandler.class)
    private String originType;

    /**
     * 题目标题
     */
    @TableField("title")
    private String title;

    /**
     * 题目内容（题干）
     */
    @TableField("content")
    private String content;

    /**
     * 题目类型（单选/多选/填空/编程/判断）
     */
    @TableField(value = "type", typeHandler = PostgresEnumTypeHandler.class)
    private String type;

    /**
     * 是否自动判分
     */
    @TableField("auto_grading")
    private Boolean autoGrading;

    /**
     * 标准答案
     */
    @TableField("expected_answer")
    private String expectedAnswer;

    /**
     * 题目分值
     */
    @TableField("score")
    private Double score;

    /**
     * 题目序号
     */
    @TableField("sequence")
    private Integer sequence;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}