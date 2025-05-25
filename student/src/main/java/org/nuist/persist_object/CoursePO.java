package org.nuist.persist_object;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 课程持久化对象
 */
@Data
@TableName("course")
public class CoursePO  {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 课程ID
     */
    @TableId(value = "course_id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 课程名称
     */
    private String name;
    
    /**
     * 课程代码
     */
    private String code;
    
    /**
     * 课程描述
     */
    private String description;
    
    /**
     * 学分
     */
    private Integer credit;
    
    /**
     * 课程类别
     */
    private String category;
    
    /**
     * 创建时间
     */
    @TableField("create_at")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField("update_at")
    private LocalDateTime updateTime;
    
    /**
     * 状态(0:禁用,1:启用)
     */
    private Integer status;
}