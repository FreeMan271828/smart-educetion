package org.nuist.po;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;

/**
 * 学生持久化对象，对应数据库student表
 */
@Data
@TableName("student")
public class StudentPO {
    
    @TableId(value = "student_id", type = IdType.ASSIGN_ID)
    private Long studentId;
    
    @TableField("username")
    private String username;
    
    @TableField("password")
    private String password;
    
    @TableField("email")
    private String email;
    
    @TableField("full_name")
    private String fullName;
    
    @TableField("phone")
    private String phone;
    
    @TableField("grade")
    private String grade;
    
    @TableField("class")
    private String className;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
} 