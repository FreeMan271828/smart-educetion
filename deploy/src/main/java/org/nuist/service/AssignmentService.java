package org.nuist.service;

import org.nuist.bo.AssignmentBO;

import java.util.List;

public interface AssignmentService {
    AssignmentBO getAssignmentById(Long assignmentId);

    List<AssignmentBO> getAssignmentsInCourseByType(Long courseId, String type);

    List<AssignmentBO> getAssignmentsInCourseByCreatorIdCourseId(Long courseId, Long creatorId);

    AssignmentBO saveAssignment(AssignmentBO assignmentBO);

    AssignmentBO updateAssignment(AssignmentBO assignmentBO);

    boolean deleteAssignment(Long assignmentId);

    List<AssignmentBO> getAssignmentsInCourseByCreatorId(Long creatorId);

    List<AssignmentBO> getAssignmentsInCourseByCreatorIdAndType(Long creatorId, String type);
}
