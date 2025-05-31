package org.nuist.service.impl;

import lombok.RequiredArgsConstructor;
import org.nuist.bo.KnowledgeBO;
import org.nuist.bo.QuestionBO;
import org.nuist.service.KnowledgeService;
import org.nuist.service.QuestionService;
import org.nuist.service.TeachingAssistantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class TeachingAssistantServiceImpl implements TeachingAssistantService {

    private final QuestionService questionService;
    private final KnowledgeService knowledgeService;

    @Override
    public QuestionBO generateQuestion(Long knowledgeId, String questionType, String difficulty) {
        if (knowledgeId == null || questionType == null || difficulty == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        }
        KnowledgeBO knowledge = knowledgeService.getKnowledgeById(knowledgeId);

        // 实际应用时，需要利用Knowledge中的信息，让大模型生成具体考核内容，此处使用模拟数据
        QuestionBO question = new QuestionBO();
        question.setKnowledgeId(knowledgeId);
        question.setQuestionType(questionType);
        question.setDifficulty(difficulty);
        question.setContent("考核题目");

        return questionService.saveQuestion(question);
    }

    @Override
    public Map<String, Object> evaluateQuestionAnswer(Long questionId, Long studentId, String answer) {
        if (questionId == null || studentId == null || !StringUtils.hasText(answer)) {
            throw new IllegalArgumentException("Parameters can't be null");
        }
        QuestionBO question = questionService.getQuestionById(questionId);

        // 实际应用时，调用大模型进行内容评测，产生结果
        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentId);
        result.put("score", 90);
        result.put("questionId", questionId);
        result.put("feedback", "结果反馈");
        result.put("errorAnalysis", "错误分析");

        return result;
    }

    @Override
    public Map<String, Object> generateTeachingPlan(String subjectType, String courseOutline, List<String> courseDocuments, Integer duration, String difficulty, String teachingStyle) {
        // 确认必要参数
        if (!StringUtils.hasText(subjectType) || !StringUtils.hasText(courseOutline) || courseDocuments == null || !StringUtils.hasText(difficulty)) {
            throw new IllegalArgumentException("Required parameters can't be null");
        }

        // 实际实现中，传参其实可以不直接传相关文档，而是传相关course的ID，然后从数据库中查询相关内容

        String prompt = ""; // 合理构建prompt

        // 根据可选参数添加要求
        if (duration != null) {
            // 添加对于教学时长的要求
            prompt += "你所生成的教案，应当使得预期的教学时长在" + duration + "分钟左右。\n";
        }

        if (StringUtils.hasText(teachingStyle)) {
            prompt += "用户期待你所生成教案的教学风格是" + teachingStyle + "\n";
        }

        // 调用大模型生成
        Map<String, Object> result = new HashMap<>(){{
            put("status", "completed");
            put("result", generateSampleLesson());
            put("generationTime", 5);
        }};
        return result;
    }

    private Map<String, Object> generateSampleLesson() {
        return new HashMap<>(){{
            put("title", "Java企业级软件开发");
            put("objectives", new ArrayList<>(){{
                add("掌握SpringBoot框架基础知识");
                add("...");
            }});
            put("keyPoints", new ArrayList<>(){{
                add("掌握SpringBoot编写Web应用");
            }});
            put("contentSections", new ArrayList<>(){{
                add(new HashMap<>(){{
                    put("title", "1.1 SpringBoot环境配置");
                    put("content", "教学内容...");
                    put("duration", 30);
                }});
            }});
        }};
    }
}
