package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nuist.handlers.PostgresEnumTypeHandler;

import java.time.LocalDateTime;

@TableName("assignment")
@Data
public class AssignmentPO {
    @TableId(type = IdType.AUTO)
    private Long assignmentId;

    /**
     * 作业类型（学生上传/教师布置）
     */
    @TableField(value = "type", typeHandler = PostgresEnumTypeHandler.class)
    private String type;

    /**
     * 创建者ID
     */
    @TableField("creator_id")
    private Long creatorId;

    /**
     * 所属课程ID
     */
    @TableField("course_id")
    private Long courseId;

    /**
     * 作业标题
     */
    @TableField("title")
    private String title;

    /**
     * 作业描述
     */
    @TableField("description")
    private String description;

    /**
     * 是否公开答案解析
     */
    @TableField("is_answer_public")
    private Boolean isAnswerPublic;

    /**
     * 是否允许查看分数
     */
    @TableField("is_score_visible")
    private Boolean isScoreVisible;

    /**
     * 是否允许重做
     */
    @TableField("is_redo_allowed")
    private Boolean isRedoAllowed;

    /**
     * 最大尝试次数
     */
    @TableField("max_attempts")
    private Integer maxAttempts;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 截止时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 作业状态（草稿/已发布/已结束）
     */
    @TableField(value = "status", typeHandler = PostgresEnumTypeHandler.class)
    private String status;

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
