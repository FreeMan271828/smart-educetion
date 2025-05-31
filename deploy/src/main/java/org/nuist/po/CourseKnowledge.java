package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("course_knowledge")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseKnowledge {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long courseId;

    private Long knowledgeId;

    private Integer sequenceNumber;

    private LocalDateTime createdAt;

}
