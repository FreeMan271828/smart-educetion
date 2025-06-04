package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件存储持久化对象
 */
@Data
@TableName("course_file")
public class CourseFilePO {
    /**
     * 文件id
     */
    @TableId(value = "file_id", type = IdType.AUTO)
    private Long fileId;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 原文件名
     */
    private String originalName;

    /**
     * 存储文件名
     */
    private String storedName;

    /**
     * 文件url
     */
    private String fileUrl;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime  createTime;


}
