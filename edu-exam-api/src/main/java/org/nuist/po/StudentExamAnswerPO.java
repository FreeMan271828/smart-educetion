package org.nuist.po;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学生考试答案持久化对象，对应数据库student_exam_answer表
 */
@Data
@TableName("student_exam_answer")
public class StudentExamAnswerPO {
    
    @TableId(value = "answer_id", type = IdType.ASSIGN_ID)
    private Long answerId;
    
    @TableField("exam_id")
    private Long examId;
    
    @TableField("question_id")
    private Long questionId;
    
    @TableField("student_id")
    private Long studentId;
    
    @TableField("student_answer")
    private String studentAnswer;
    
    @TableField("score")
    private BigDecimal score;
    
    @TableField("feedback")
    private String feedback;
    
    @TableField("graded_by")
    private Long gradedBy;
    
    @TableField("graded_at")
    private LocalDateTime gradedAt;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
} 