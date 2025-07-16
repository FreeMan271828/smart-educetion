package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apiguardian.api.API;
import org.nuist.config.WebClientConfig;
import org.nuist.dto.ChatHistoryRequest;
import org.nuist.service.StudentAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * 学生学习助手控制器
 */
@Slf4j
@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/student-assistant")
public class StudentAssistantController {
    public final WebClient webClient;


    @Autowired
    private StudentAssistantService studentAssistantService;

    @Autowired
    public StudentAssistantController(WebClient webClient) {
        this.webClient = webClient;
    }


    /**
     * 一次性问答
     * 问答采用非流式，在答案生成之后一次性返回
     * 不考虑上下文结构
     * @param studentId 学生ID
     * @param question 问题内容
     * @param courseId 相关课程ID(可选)
     * @return 问答结果
     */
    @Operation(summary = "无历史非流式一次性问答")
    @PostMapping("/student/{studentId}/ask")
    public ResponseEntity<Map<String, Object>> askQuestion(
            @PathVariable("studentId") Long studentId,
            @RequestParam("question") String question,
            @RequestParam(value = "courseId", required = false) Long courseId) {
        Map<String, Object> answer = studentAssistantService.askQuestion(studentId, question, courseId);
        return ResponseEntity.ok(answer);
    }


    /**
     * 带历史记录的问答接口
     * 支持多轮上下文对话（非流式）
     * @param studentId 学生ID
     * @param request 包含历史消息和当前问题的请求体
     * @return 问答结果
     */
    @Operation(summary = "带历史非流式问答")
    @PostMapping("/student/{studentId}/ask/history")
    public ResponseEntity<Map<String, Object>> askQuestionWithHistory(
            @PathVariable("studentId") Long studentId,
            @RequestBody ChatHistoryRequest request) {

        // 验证必要参数
        if (studentId == null || request == null || !request.isValid()) {
            return ResponseEntity.badRequest().body(Map.of("error", "无效请求参数"));
        }


        // 调用带历史记录的问答服务
        Map<String, Object> response = studentAssistantService.askWithHistory(
                studentId,
                request.getMessages()
        );

        return ResponseEntity.ok(response);
    }


    /**
     * 带历史消息的流式问答接口
     *
     * @param request 包含历史消息的请求体
     * @return SSE流式响应
     */
    @Operation(summary = "带历史流式问答")
    @PostMapping(value = "/stream/chat-history",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestBody ChatHistoryRequest request) {
        return webClient.post()
                .uri("/chat/stream")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .bodyValue(convertToFastApiFormat(request))
                .exchangeToFlux(response -> {
                    return response.body(BodyExtractors.toDataBuffers())
                            .map(dataBuffer -> {
                                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(bytes);
                                DataBufferUtils.release(dataBuffer);

                                // 关键修复：清洗多余格式
                                String rawData = new String(bytes, StandardCharsets.UTF_8);
                                return cleanSseFormat(rawData);
                            })
                            .filter(data -> !data.isEmpty()) // 过滤空数据
                            .doOnNext(data -> log.info("清洗后数据: {}", data));
                })
                .onErrorResume(e -> {
                    log.error("处理失败: {}", e.getMessage());
                    return Flux.just("event: error\ndata: " + e.getMessage() + "\n\n");
                });
    }

    // SSE格式清洗工具方法
    private String cleanSseFormat(String rawData) {
        // 情况1：已经是完整SSE格式 -> 直接返回
        if (rawData.startsWith("data: ") && rawData.contains("\n\n")) {
            return rawData;
        }

        // 情况2：Python部分封装 -> 移除多余"data: "前缀
        if (rawData.startsWith("data: ")) {
            // 示例：data: 量子\n -> 转为 量子
            return rawData.substring(6).trim();
        }

        // 情况3：原始数据 -> 添加标准SSE格式
        return "data: " + rawData + "\n\n";
    }
    /**
     * 将Java对象转换为FastAPI所需的格式
     */
    private Object convertToFastApiFormat(ChatHistoryRequest request) {
        return new Object() {
            public final List<FastApiMessage> messages = request.getMessages().stream()
                    .map(m -> new FastApiMessage(m.getRole(), m.getContent()))
                    .toList();
        };
    }

    /**
     * 内部类：FastAPI所需的Message格式
     */
    private static class FastApiMessage {
        private final String role;
        private final String content;

        public FastApiMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }






    
    /**
     * 基于课程名称生成练习
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @param difficultyLevel 难度级别(可选)
     * @param questionCount 题目数量
     * @return 练习内容
     */
    @Operation(summary = "基于课程名称生成练习")
    @GetMapping("/student/{studentId}/generate-exercise/by-course")
    public ResponseEntity<Map<String, Object>> generateExerciseByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("courseName") String courseName,
            @RequestParam(value = "difficultyLevel", required = false) String difficultyLevel,
            @RequestParam("problemCount") Integer questionCount) {
        Map<String, Object> exercise = studentAssistantService.generateExerciseByCourseName(studentId, courseName, 
                                                                                      difficultyLevel, questionCount);
        return ResponseEntity.ok(exercise);
    }
    
    /**
     * 基于知识点名称生成练习
     * @param studentId 学生ID
     * @param knowledgeNames 知识点名称列表
     * @param difficultyLevel 难度级别(可选)
     * @param questionCount 题目数量
     * @return 练习内容
     */
    @Operation(summary = "基于知识点名称生成练习")
    @GetMapping("/student/{studentId}/generate-exercise/by-knowledge")
    public ResponseEntity<Map<String, Object>> generateExerciseByKnowledgeNames(
            @PathVariable("studentId") Long studentId,
            @RequestParam("knowledgeNames") List<String> knowledgeNames,
            @RequestParam(value = "difficultyLevel", required = false) String difficultyLevel,
            @RequestParam("problemCount") Integer questionCount) {
        Map<String, Object> exercise = studentAssistantService.generateExerciseByKnowledgeNames(studentId, knowledgeNames, 
                                                                                          difficultyLevel, questionCount);
        return ResponseEntity.ok(exercise);
    }

} 