package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apiguardian.api.API;
import org.nuist.bo.ProblemBO;
import org.nuist.bo.StudentProblemAnswerBO;
import org.nuist.constant.ProblemType;
import org.nuist.service.ProblemService;
import org.nuist.service.StudentProblemAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.nuist.constant.GradingStatus;

import java.util.*;
import java.util.stream.Collectors;

import static org.nuist.constant.ProblemType.*;


@Slf4j
@RestController
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "student-answer", description = "学生答题管理API")
@RequestMapping("/api/student-answer")
public class StudentProblemAnswerController {
    @Autowired
    private ProblemService problemService;

    @Autowired
    private StudentProblemAnswerService studentProblemAnswerService;

    // 1. 查询对应题目的作答记录
    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<StudentProblemAnswerBO>> getAnswersByProblem(
            @PathVariable Long problemId
    ) {
        return ResponseEntity.ok(studentProblemAnswerService.getAnswersByProblem(problemId));
    }

    // 2. 查询对应作业的所有题目的作答记录
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<StudentProblemAnswerBO>> getAnswersByAssignment(
            @PathVariable Long assignmentId
    ) {
        return ResponseEntity.ok(studentProblemAnswerService.getAnswersByAssignment(assignmentId));
    }

    // 3. 作答对应题目
    @PostMapping("/submit")
    public ResponseEntity<StudentProblemAnswerBO> submitAnswer(
            @RequestParam Long studentId,
            @RequestParam Long problemId,
            @RequestParam String answer
    ) { String cleanedAnswer = answer.trim().replaceAll("\\s+", " ");
        log.info("用户答案：{}", cleanedAnswer);
        StudentProblemAnswerBO result = studentProblemAnswerService.submitAnswer(studentId, problemId, cleanedAnswer);

        // 直接使用ProblemBO
        ProblemBO problem = problemService.getProblemById(problemId);
        log.info("题目类型：{}", problem.getType());
        log.info("题目类型 {}", FILL_BLANK);
        log.info("自动判分类型 {}", problem.getAutoGrading());
        if (problem.getAutoGrading() &&
                (Objects.equals(problem.getType(), SINGLE_CHOICE) ||
                        Objects.equals(problem.getType(), MULTI_CHOICE) ||
                        Objects.equals(problem.getType(), FILL_BLANK) ||
                        Objects.equals(problem.getType(), TRUE_FALSE))) {

            String standardAnswer = problem.getExpectedAnswer().trim();

            log.info("标准答案：{}", standardAnswer);

            boolean isCorrect = false;

            switch (problem.getType()) {
                case SINGLE_CHOICE:
                case TRUE_FALSE:
                    isCorrect = cleanedAnswer.equalsIgnoreCase(standardAnswer);
                    break;
                case MULTI_CHOICE:
                    // 多选题：拆分选项后比较集合（忽略顺序和大小写）
                    Set<String> studentChoices = Arrays.stream(cleanedAnswer.split(" "))
                            .map(String::trim)
                            .map(String::toUpperCase)
                            .collect(Collectors.toSet());

                    Set<String> standardChoices = Arrays.stream(standardAnswer.split(" "))
                            .map(String::trim)
                            .map(String::toUpperCase)
                            .collect(Collectors.toSet());

                    isCorrect = studentChoices.equals(standardChoices);
                    break;
                case FILL_BLANK:
                    // 填空题：严格比较（可考虑未来扩展部分匹配）
                    isCorrect = cleanedAnswer.equals(standardAnswer);
                    break;
            }
            result.setIsAutoGraded(true);
            result.setAutoScore(isCorrect ? problem.getScore() : 0.0);
            result.setGradingStatus(GradingStatus.SUCCESS);

            studentProblemAnswerService.autoUpdateAnswerResult(result.toPO());
            log.info("自动批改结果：{}", isCorrect);
        }
        log.info("执行完成");
        return ResponseEntity.ok(result);
    }

    // 4. 批改对应题目答案
    @PostMapping("/grade")
    public ResponseEntity<StudentProblemAnswerBO> gradeAnswer(
            @RequestParam Long answerId,
            @RequestParam Double score
    ) {
        return ResponseEntity.ok(studentProblemAnswerService.gradeAnswer(answerId, score));
    }

    // 5. 查询完成率

    @GetMapping("/completion-rate")
    public ResponseEntity<Map<String, Object>> getCompletionRate(
            @RequestParam Long studentId,
            @RequestParam Long assignmentId
    ) {
        return ResponseEntity.ok(studentProblemAnswerService.getCompletionRate(studentId, assignmentId));
    }

    // 6. 查询正确率

    @GetMapping("/accuracy-rate")
    public ResponseEntity<Map<String, Object>> getAccuracyRate(
            @RequestParam Long studentId,
            @RequestParam Long assignmentId
    ) {
        return ResponseEntity.ok(studentProblemAnswerService.getAccuracyRate(studentId, assignmentId));
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long answerId) {
        studentProblemAnswerService.deleteAnswer(answerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/final-score/{answerId}")
    public ResponseEntity<Double> getFinalScoreByAnswerId(@PathVariable Long answerId) {
        return ResponseEntity.ok(studentProblemAnswerService.getFinalScoreByAnswerId(answerId));
    }

    @GetMapping("/final-scores/assignment/{assignmentId}")
    public ResponseEntity<List<Double>> getFinalScoresByAssignmentId(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(studentProblemAnswerService.getFinalScoresByAssignmentId(assignmentId));
    }
}