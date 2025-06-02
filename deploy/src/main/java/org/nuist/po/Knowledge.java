package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("knowledge")
@Data
public class Knowledge {

    @TableId(type = IdType.AUTO)
    private Long knowledgeId;

    /**
     * 知识点名称
     */
    private String name;

    /**
     * 知识点具体描述
     */
    private String description;

    /**
     * 知识点难度
     */
    private String difficultyLevel;

    /**
     * 负责该知识点课件的教师ID
     */
    private Long teacherId;

    /**
     * 教案内容
     */
    private String teachPlan;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
