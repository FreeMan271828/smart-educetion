package org.nuist.po;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学习进度持久化对象，对应数据库learning_progress表
 */
@Data
@TableName("learning_progress")
public class LearningProgressPO {
    
    @TableId(value = "progress_id", type = IdType.ASSIGN_ID)
    private Long progressId;
    
    @TableField("student_id")
    private Long studentId;
    
    @TableField("course_id")
    private Long courseId;
    
    @TableField("knowledge_id")
    private Long knowledgeId;
    
    @TableField("mastery_level")
    private BigDecimal masteryLevel;
    
    @TableField("learning_status")
    private String learningStatus;
    
    @TableField("last_reviewed_at")
    private LocalDateTime lastReviewedAt;
    
    @TableField("review_count")
    private Integer reviewCount;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
} 