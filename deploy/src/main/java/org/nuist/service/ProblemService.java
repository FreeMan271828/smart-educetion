package org.nuist.service;

import org.nuist.bo.ProblemBO;

import java.util.List;

public interface ProblemService {
    ProblemBO getProblemById(Long problemId);

    List<ProblemBO> getProblemsByAssignmentId(Long assignmentId);

    List<ProblemBO> getProblemsByAssignmentIdAndType(Long assignmentId, String type);

    List<ProblemBO> getProblemsByType(String type);

    ProblemBO saveProblem(ProblemBO problemBO);

    ProblemBO updateProblem(ProblemBO problemBO);

    boolean deleteProblem(Long problemId);
}
