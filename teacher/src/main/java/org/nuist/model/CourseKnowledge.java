package org.nuist.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("course_knowledge")
@Data
public class CourseKnowledge {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long courseId;

    private Long knowledgeId;

    private Integer sequenceNumber;

    private LocalDateTime createdAt;

}
