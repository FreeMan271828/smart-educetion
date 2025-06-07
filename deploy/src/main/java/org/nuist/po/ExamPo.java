package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("exam")
@Data
public class ExamPo {

    @TableId(type = IdType.AUTO)
    private Long examId;

    /**
     * 考试标题
     */
    @TableField("title")
    private String title;

    /**
     * 考试描述
     */
    @TableField("description")
    private String description;

    /**
     * 考试所属课程ID
     */
    @TableField("course_id")
    private Long courseId;

    /**
     * 考试关联的教师ID
     */
    @TableField("teacher_id")
    private Long teacherId;

    /**
     * 考试、课后习题等类型
     */
    private String type;

    /**
     * 若是课后习题，则记录关联的知识点
     */
    private Long knowledgeId;

    /**
     * 试卷满分
     */
    @TableField("total_score")
    private Long totalScore;

    /**
     * 本次考试允许的最多时长
     */
    @TableField("duration_minutes")
    private Integer durationMinutes;

    /**
     * 考试开放时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 考试关闭时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    @TableField("status")
    private String status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

}
