package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@TableName("learning_plan")
public class LearningPlanPO {
    @TableId(type = IdType.AUTO)
    private Long planId;

    @TableField("student_id")
    private Long studentId;

    @TableField("name")
    private String name;

    @TableField("content")
    private String content;

    @TableField("begin_at")
    private LocalDateTime beginAt;

    @TableField("end_at")
    private LocalDateTime endAt;

    @TableField("completed")
    private boolean completed;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
