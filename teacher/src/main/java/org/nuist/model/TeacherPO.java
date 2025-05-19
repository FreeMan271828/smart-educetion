package org.nuist.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("teacher")
@Data
public class TeacherPO {

    @TableId(type = IdType.AUTO)
    private Long teacherId;

    private String username;

    private String password;

    private String email;

    private String fullName;

    private String phone;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
