package org.nuist.service;

import org.nuist.bo.StudentProblemAnswerBO;
import org.nuist.po.StudentProblemAnswerPO;

import java.util.List;
import java.util.Map;

public interface StudentProblemAnswerService {
    List<StudentProblemAnswerBO> getAnswersByProblem(Long problemId);

    List<StudentProblemAnswerBO> getAnswersByAssignment(Long assignmentId);

    StudentProblemAnswerBO submitAnswer(Long studentId, Long problemId, String answer);

    StudentProblemAnswerBO gradeAnswer(Long answerId, Double score);

    Map<String, Object> getCompletionRate(Long studentId, Long assignmentId);

    Map<String, Object> getAccuracyRate(Long studentId, Long assignmentId);

    Double getFinalScoreByAnswerId(Long answerId);

    List<Double> getFinalScoresByAssignmentId(Long assignmentId);

    void autoUpdateAnswerResult(StudentProblemAnswerPO po);

    void deleteAnswer(Long answerId);
}
