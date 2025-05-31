package org.nuist.po;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 考勤持久化对象，对应数据库attendance表
 */
@Data
@TableName("attendance")
public class AttendancePO {
    
    @TableId(value = "attendance_id", type = IdType.ASSIGN_ID)
    private Long attendanceId;
    
    @TableField("course_id")
    private Long courseId;
    
    @TableField("student_id")
    private Long studentId;
    
    @TableField("status")
    private String status;
    
    @TableField("attendance_date")
    private LocalDate attendanceDate;
    
    @TableField("remark")
    private String remark;
    
    @TableField("recorded_by")
    private Long recordedBy;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
} 