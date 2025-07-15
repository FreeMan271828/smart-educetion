package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nuist.bo.StudentProblemAnswerBO;
import org.nuist.constant.AnswerStatus;
import org.nuist.constant.GradingStatus;
import org.nuist.mapper.AssignmentMapper;
import org.nuist.mapper.ProblemMapper;
import org.nuist.mapper.StudentProblemAnswerMapper;
import org.nuist.po.AssignmentPO;
import org.nuist.po.ProblemPO;
import org.nuist.po.StudentProblemAnswerPO;
import org.nuist.service.StudentProblemAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentProblemAnswerServiceImpl implements StudentProblemAnswerService {

    @Autowired
    private StudentProblemAnswerMapper answerMapper;

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    private ProblemMapper problemMapper;

    @Override
    public List<StudentProblemAnswerBO> getAnswersByProblem(Long problemId) {
        if (problemId == null) {
            return List.of();
        }

        LambdaQueryWrapper<StudentProblemAnswerPO> query = Wrappers.lambdaQuery();
        query.eq(StudentProblemAnswerPO::getProblemId, problemId)
                .orderByAsc(StudentProblemAnswerPO::getCreatedAt);

        return answerMapper.selectList(query).stream()
                .map(StudentProblemAnswerBO::fromPO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentProblemAnswerBO> getAnswersByAssignment(Long assignmentId) {
        if (assignmentId == null) {
            return List.of();
        }

        LambdaQueryWrapper<StudentProblemAnswerPO> query = Wrappers.lambdaQuery();
        query.eq(StudentProblemAnswerPO::getAssignmentId, assignmentId)
                .orderByAsc(StudentProblemAnswerPO::getProblemId);

        return answerMapper.selectList(query).stream()
                .map(StudentProblemAnswerBO::fromPO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentProblemAnswerBO submitAnswer(Long studentId, Long problemId, String answer) {
        if (studentId == null || problemId == null || answer == null) {
            throw new IllegalArgumentException("学生ID、题目ID和答案不能为空");
        }

        // 获取题目信息
        ProblemPO problem = problemMapper.selectById(problemId);
        if (problem == null) {
            throw new IllegalArgumentException("题目不存在: " + problemId);
        }

        // 获取作业信息（用于检查最大尝试次数）
        AssignmentPO assignment = assignmentMapper.selectById(problem.getAssignmentId());
        if (assignment == null) {
            throw new IllegalArgumentException("作业不存在: " + problem.getAssignmentId());
        }

        // 查询现有答题记录
        LambdaQueryWrapper<StudentProblemAnswerPO> query = Wrappers.lambdaQuery();
        query.eq(StudentProblemAnswerPO::getStudentId, studentId)
                .eq(StudentProblemAnswerPO::getProblemId, problemId);

        StudentProblemAnswerPO existing = answerMapper.selectOne(query);

        // 如果没有记录，创建新记录
        if (existing == null) {
            return createNewAnswer(studentId, problem, assignment, answer);
        }

        // 如果有记录，检查尝试次数
        return updateExistingAnswer(existing, assignment, answer);
    }

    @Override
    public StudentProblemAnswerBO gradeAnswer(Long answerId, Double score) {
        if (answerId == null || score == null) {
            throw new IllegalArgumentException("答案ID和分数不能为空");
        }

        // 获取答题记录
        StudentProblemAnswerPO answer = answerMapper.selectById(answerId);
        if (answer == null) {
            throw new IllegalArgumentException("答题记录不存在: " + answerId);
        }

        // 更新分数和状态
        answer.setManualScore(score);
        answer.setGradingStatus(GradingStatus.SUCCESS);
        answer.setUpdatedAt(LocalDateTime.now());

        // 更新数据库
        int result = answerMapper.updateById(answer);
        if (result <= 0) {
            throw new RuntimeException("批改失败");
        }

        return StudentProblemAnswerBO.fromPO(answer);
    }

    public Map<String, Object> getCompletionRate(Long studentId, Long assignmentId) {
        // 获取作业总题目数（不变）
        LambdaQueryWrapper<ProblemPO> problemQuery = Wrappers.lambdaQuery();
        problemQuery.eq(ProblemPO::getAssignmentId, assignmentId);
        int totalProblems = Math.toIntExact(problemMapper.selectCount(problemQuery));

        if (totalProblems == 0) {
            return Map.of("completionRate", 0.0);
        }

        // 调用 Mapper 的自定义方法（避免 Lambda 枚举）
        int completedProblems = answerMapper.countSubmittedAnswers(
                studentId,
                assignmentId,
                AnswerStatus.SUBMITTED // 传递枚举名称而非对象
        );

        double completionRate = (double) completedProblems / totalProblems;
        return Map.of("completionRate", completionRate,
                        "totalProblems", totalProblems,
                "completedProblems", completedProblems
        );
    }

    @Override
    public Map<String, Object> getAccuracyRate(Long studentId, Long assignmentId) {
        if (studentId == null || assignmentId == null) {
            return Map.of("accuracyRate", 0.0);
        }

        // 获取学生所有答题记录无论是否已经批改
        LambdaQueryWrapper<StudentProblemAnswerPO> query = Wrappers.lambdaQuery();
        query.eq(StudentProblemAnswerPO::getStudentId, studentId)
                .eq(StudentProblemAnswerPO::getAssignmentId, assignmentId);


        List<StudentProblemAnswerPO> answers = answerMapper.selectList(query);

        if (answers.isEmpty()) {
            return Map.of("accuracyRate", 0.0);
        }

        // 计算正确题目数（得分 > 0）
        long correctAnswers = answers.stream()
                .filter(answer -> answer.getFinalScore() != null && answer.getFinalScore() > 0)
                .count();

        // 计算正确率
        double accuracyRate = (double) correctAnswers / answers.size();

        return Map.of(
                "studentId", studentId,
                "assignmentId", assignmentId,
                "correctAnswers", correctAnswers,
                "totalGradedAnswers", answers.size(),
                "accuracyRate", accuracyRate
        );
    }

    // 创建新答题记录
    private StudentProblemAnswerBO createNewAnswer(
            Long studentId, ProblemPO problem, AssignmentPO assignment, String answer
    ) {
        StudentProblemAnswerPO newAnswer = new StudentProblemAnswerPO();
        newAnswer.setStudentId(studentId);
        newAnswer.setAssignmentId(problem.getAssignmentId());
        newAnswer.setProblemId(problem.getProblemId());
        newAnswer.setAnswer(answer);
        newAnswer.setAttemptNumber(1);
        newAnswer.setStatus(AnswerStatus.SUBMITTED);
        newAnswer.setGradingStatus(GradingStatus.PENDING);
        newAnswer.setCreatedAt(LocalDateTime.now());
        newAnswer.setUpdatedAt(LocalDateTime.now());

        // 设置自动批改标志
        newAnswer.setIsAutoGraded(false);

        // 插入数据库
        int result = answerMapper.insert(newAnswer);
        if (result <= 0) {
            throw new RuntimeException("提交答案失败");
        }

        return StudentProblemAnswerBO.fromPO(newAnswer);
    }

    // 更新现有答题记录
    private StudentProblemAnswerBO updateExistingAnswer(
            StudentProblemAnswerPO existing, AssignmentPO assignment, String answer
    ) {
        // 检查尝试次数上限
        if (existing.getAttemptNumber() >= assignment.getMaxAttempts()) {
            throw new IllegalStateException("已达到最大尝试次数: " + assignment.getMaxAttempts());
        }

        // 更新答案和尝试次数
        existing.setAnswer(answer);
        existing.setAttemptNumber(existing.getAttemptNumber() + 1);
        existing.setStatus(AnswerStatus.SUBMITTED);
        existing.setGradingStatus(GradingStatus.PENDING);
        existing.setUpdatedAt(LocalDateTime.now());

        // 更新数据库
        int result = answerMapper.updateById(existing);
        if (result <= 0) {
            throw new RuntimeException("更新答案失败");
        }

        return StudentProblemAnswerBO.fromPO(existing);
    }

    @Override
    public Double getFinalScoreByAnswerId(Long answerId) {
        return answerMapper.getFinalScoreByAnswerId(answerId);
    }

    @Override
    public List<Double> getFinalScoresByAssignmentId(Long assignmentId) {
        return answerMapper.getFinalScoresByAssignmentId(assignmentId);
    }

    @Override
    @Transactional
    public void autoUpdateAnswerResult(StudentProblemAnswerPO po) {
        po.setUpdatedAt(LocalDateTime.now());
        int affectedRows = answerMapper.updateById(po);  // 根据主键更新
        if (affectedRows == 0) {
            throw new IllegalStateException("答案记录更新失败，ID：" + po.getAnswerId());
        }
    }

    @Override
    public void deleteAnswer(Long answerId) {

        int result = answerMapper.deleteById(answerId);
        if (result <= 0) {
            throw new RuntimeException("删除答案失败");
        }
    }
}