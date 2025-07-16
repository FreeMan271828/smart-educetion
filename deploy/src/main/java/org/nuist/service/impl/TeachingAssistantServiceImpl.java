package org.nuist.service.impl;

import lombok.RequiredArgsConstructor;
import org.nuist.bo.*;
import org.nuist.dto.request.ChatRequestDto;
import org.nuist.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeachingAssistantServiceImpl implements TeachingAssistantService {

    private final KnowledgeService knowledgeService;
    private final LearningProgressService learningProgressService;
    private final ExamService examService;
    private final StudentExamService studentExamService;
    private final WebClient webClient;

    @Override
    public Map<String, Object> generateTeachingPlan(String subjectType, String courseOutline, Integer duration, String difficulty, String teachingStyle) {
        // 确认必要参数
        if (!StringUtils.hasText(subjectType) || !StringUtils.hasText(courseOutline) || !StringUtils.hasText(difficulty)) {
            throw new IllegalArgumentException("Required parameters can't be null");
        }

        // 合理构建prompt
        String prompt = """
                你需要根据下面的需求，进行教师教学方案内容的生成。具体需求如下：
                
                学科类型是：%s
                课程需求大纲如下：
                %s
                
                """.formatted(subjectType, courseOutline);

        // 根据可选参数添加要求
        if (duration != null) {
            // 添加对于教学时长的要求
            prompt += "你所生成的教案，应当使得预期的教学时长在" + duration + "分钟左右。\n";
        }

        if (StringUtils.hasText(teachingStyle)) {
            prompt += "用户期待你所生成教案的教学风格是：" + teachingStyle + "\n";
        }

        prompt += """
                你需要生成一个完整的教师教学方案内容，需要包括如下板块：
                1. 课程标题
                2. 课程目标
                3. 教学难点和重点
                4. 具体教学内容：每小节需要包含小标题、内容和建议学习时长
                5. 建议的教师互动环节（可选）
                
                请你仅输出如上所描述的教学方案内容，不要输出其他内容。
                """;

        AiMessage inputMessage = AiMessage.builder()
                .role("user")
                .content(prompt)
                .build();

        ChatRequestDto dto = new ChatRequestDto();
        dto.setMessages(List.of(inputMessage));

        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("invokeLLM");

        ResponseEntity<Map> result = webClient.post()
                .uri("/chat")
                .bodyValue(dto).retrieve().toEntity(Map.class).block();
        if (result == null || result.getBody() == null) {
            stopWatch.stop();
            return new HashMap<>(){{
               put("status", "error");
               put("message", "Response is null");
            }};
        }

        stopWatch.stop();

        // 调用大模型生成
        return new HashMap<>(){{
            put("status", "completed");
            put("result", result.getBody().get("answer"));
            put("generationTime", stopWatch.getTotalTime(TimeUnit.SECONDS));
        }};
    }

    @Override
    public Map<String, Object> improveTeachingPlan(String previousPlan, String suggestion) {
        if (!StringUtils.hasText(previousPlan) || !StringUtils.hasText(suggestion)) {
            throw new IllegalArgumentException("Required parameters can't be null");
        }
        List<AiMessage> messages = new ArrayList<>();
        messages.add(
                AiMessage.builder()
                        .role("user")
                        .content("""
                                下面是一份教学教案，我需要对它进行更改、优化。请你先阅读原教案内容。
                                
                                %s
                                """.formatted(previousPlan))
                        .build()
        );
        messages.add(
                AiMessage.builder()
                        .role("assistant")
                        .content("好的，我已经看到并了解了原教案内容，你需要如何修改？")
                        .build()
        );
        messages.add(
                AiMessage.builder()
                        .role("user")
                        .content("""
                                %s
                                
                                请你根据上面的建议和原教案内容，进行教案的优化。你只需要生成优化后的教案内容，并确保仍包含这些板块：
                                1. 课程标题
                                2. 课程目标
                                3. 教学难点和重点
                                4. 具体教学内容：每小节需要包含小标题、内容和建议学习时长
                                5. 建议的教师互动环节（可选）
                                """.formatted(suggestion))
                        .build()
        );

        ChatRequestDto dto = new ChatRequestDto();
        dto.setMessages(messages);

        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ResponseEntity<Map> result = webClient.post().uri("/chat")
                .bodyValue(dto).retrieve().toEntity(Map.class).block();
        if (result == null || result.getBody() == null) {
            stopWatch.stop();
            return new HashMap<>(){{
                put("status", "error");
                put("message", "Response is null");
            }};
        }

        stopWatch.stop();

        return new HashMap<>(){{
            put("status", "completed");
            put("result", result.getBody().get("answer"));
            put("generationTime", stopWatch.getTotalTime(TimeUnit.SECONDS));
        }};
    }

    @Override
    public Map<String, Object> analyzeCourseLearning(Long courseId, Long studentId) {
        if (courseId == null || studentId == null) {
            throw new IllegalArgumentException("Required parameters can't be null");
        }
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("getDatabaseInfo");

        List<KnowledgeBO> knowledgeInCourse = knowledgeService.getKnowledgeByCourseId(courseId);
        if (knowledgeInCourse.isEmpty()) {
            return new HashMap<>(){{
                put("status", "warning");
                put("message", "该课程中还没有任何知识点可追踪进度");
            }};
        }

        Map<Long, KnowledgeBO> knowledgeMap = knowledgeInCourse.stream()
                .collect(Collectors.toMap(
                        KnowledgeBO::getKnowledgeId,
                        k -> k,
                        (k1, k2) -> k1
                ));
        List<LearningProgressBO> progress = learningProgressService.getStudentCourseProgress(studentId, courseId);

        StringBuilder prompt = new StringBuilder("你需要根据一位学生的一系列课程知识学习进度情况、作业考试完成情况，来综合分析该生的学习情况，包括学习总体进度；优势弱势领域；评语和改进建议。\n\n");

        // 课程部分
        prompt.append("### 该课程现在包含如下的知识点：\n\n");
        for (KnowledgeBO knowledge : knowledgeInCourse) {
            prompt.append("- ")
                    .append(knowledge.getName()).append(" - ")
                    .append(knowledge.getDescription())
                    .append(knowledge.getDifficultyLevel()).append("\n");
        }

        // 学习进度部分
        prompt.append("\n### 该生现在具有如下的学习进度：\n\n");
        for (LearningProgressBO p : progress) {
            if (knowledgeMap.containsKey(p.getKnowledgeId())) {
                prompt.append("- ")
                        .append(knowledgeMap.get(p.getKnowledgeId()).getName()).append("进度：")
                        .append(p.getMasteryLevel()).append(" | ").append(p.getLearningStatus()).append("\n");
            }
        }

        // 考核部分
        List<ExamBO> examsInCourse = examService.getExamsByCourseId(courseId);
        List<ExamBO> answeredExams = examsInCourse.stream().filter(
                // 选出该生已经作答的考核
                exam ->
                        studentExamService.getIfExamAnsweredByStudent(studentId, exam.getExamId())
        ).toList();
        prompt.append("\n### 该生现在具有如下的课程考核（考试、作业）情况：\n\n");
        for (ExamBO exam : answeredExams) {
            BigDecimal score = studentExamService.getExamScore(studentId, exam.getExamId());
            // 一定程度上筛选不合法数据
            if (!BigDecimal.ZERO.equals(score)) {
                prompt.append("- ")
                        .append(exam.getType()).append(": ")
                        .append(exam.getTitle()).append(" - ")
                        .append(exam.getDescription()).append("，分数：")
                        .append(score).append("/").append(exam.getTotalScore()).append("\n");
            }
        }

        stopWatch.stop();
        double searchDatabaseTimeSeconds = stopWatch.getTotalTime(TimeUnit.SECONDS);

        prompt.append("""
                
                请你结合上述所有信息，评判该学生的学习情况，不要生成多余内容。需要包括这些方面：
                1. 学生学习该课程的优势领域和弱势领域
                2. 学生学习该课程目前的总体进度
                3. 给该学生本课程学习提出的建议
                """);

        ChatRequestDto dto = new ChatRequestDto();
        dto.setMessages(List.of(
                AiMessage.builder()
                        .role("user")
                        .content(prompt.toString())
                        .build()
        ));

        stopWatch = new StopWatch();
        stopWatch.start("queryLLM");
        ResponseEntity<Map> result = webClient.post().uri("/chat").bodyValue(dto).retrieve().toEntity(Map.class).block();
        if (result == null || result.getBody() == null) {
            stopWatch.stop();
            return new HashMap<>(){{
                put("status", "error");
                put("message", "Response is null");
            }};
        }
        stopWatch.stop();
        StopWatch finalStopWatch = stopWatch;
        return new HashMap<>(){{
            put("status", "completed");
            put("result", result.getBody().get("answer"));
            put("getDatabaseTimeSpentSeconds", searchDatabaseTimeSeconds);
            put("queryLLMTimeSpentSeconds", finalStopWatch.getTotalTime(TimeUnit.SECONDS));
        }};
    }
}
