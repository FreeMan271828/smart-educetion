package org.nuist.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("exam")
@Data
public class Exam {

    @TableId(type = IdType.AUTO)
    private Long examId;

    /**
     * 考试标题
     */
    private String title;

    /**
     * 考试描述
     */
    private String description;

    /**
     * 考试所属课程ID
     */
    private Long courseId;

    /**
     * 考试关联的教师ID
     */
    private Long teacherId;

    /**
     * 试卷满分
     */
    private Integer totalScore;

    /**
     * 本次考试允许的最多时长
     */
    private Integer durationMinutes;

    /**
     * 考试开放时间
     */
    private LocalDateTime startTime;

    /**
     * 考试关闭时间
     */
    private LocalDateTime endTime;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
