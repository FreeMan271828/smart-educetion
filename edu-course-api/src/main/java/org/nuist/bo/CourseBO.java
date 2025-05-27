package org.nuist.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程业务对象
 */
@Data
public class CourseBO {
    
    /**
     * 课程ID
     */
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
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 状态(0:禁用,1:启用)
     */
    private Integer status;
} 