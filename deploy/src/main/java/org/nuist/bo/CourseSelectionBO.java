package org.nuist.bo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.CoursePO;
import org.nuist.po.CourseSelectionPO;

import java.time.LocalDateTime;

/**
 *  课程选课业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseSelectionBO {


    private Long id;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    public CourseSelectionPO toPO() {
        CourseSelectionPO po = new CourseSelectionPO();
         po.setId(this.id);
         po.setStudentId(this.studentId);
         po.setCourseId(this.courseId);
         po.setCreatedTime(this.createdTime);
         return po;
    }
}
