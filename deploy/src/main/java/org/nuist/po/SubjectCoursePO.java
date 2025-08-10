package org.nuist.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("subject_course")
public class SubjectCoursePO {
    @TableId(type=com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Long id;

    private Long subjectId;

    private Long courseId;

    private LocalDateTime createdAt;
}
