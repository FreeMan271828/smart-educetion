package org.nuist.service;

import org.nuist.bo.SubjectBO;

import java.util.List;
import java.util.Map;

public interface SubjectService {
    List<SubjectBO> searchSubjects(String keywords);

    SubjectBO getSubjectById(Long id);

    Map<String,Object> addSubject(SubjectBO subjectBO);

    Map<String, Object> updateSubject(SubjectBO subjectBO);

    Map<String, Object> deleteSubject(Long id);

    Map<String, Object> addSubjectCourseRelation(Long subjectId, Long courseId);

    List<SubjectBO> getCoursesBySubjectId(Long subjectId);

    SubjectBO getSubjectByCourseId(Long courseId);

    Map<String, Object> deleteSubjectCourseRelation(Long subjectId, Long courseId);

    List<SubjectBO> getAllSubjects();
}
