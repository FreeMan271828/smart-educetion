package org.nuist.service;

import org.nuist.bo.CourseBO;
import org.nuist.bo.StudentBO;

import java.util.List;

/**
 * 学生选课接口
 */
public interface CourseSelectionService {
    List<CourseBO> getCourseSelections(Long studentId);

    Long saveCourseSelection(Long studentId, Long courseId);

    boolean deleteCourseSelection(Long studentId, Long courseId);

    List<StudentBO> getStudentsByCourseId(Long courseId);

    boolean DeleteAllCourseSelection(Long courseId);

    boolean isCourseSelected(Long studentId, Long courseId);
}
