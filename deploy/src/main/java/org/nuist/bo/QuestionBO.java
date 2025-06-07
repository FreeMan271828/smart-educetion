package org.nuist.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.po.QuestionPo;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBO {

    private Long questionId;
    private Long teacherId;
    private String content;
    private String questionType;
    private String difficulty;
    @Schema(description = "问题关联的知识点ID | 该字段仅做预留，目前不应当有任何业务作用")
    private Long knowledgeId;
    private Long examId;
    private String referenceAnswer;
    private Long scorePoints;
    private String answer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static QuestionBO fromQuestion(QuestionPo questionPo) {
        return QuestionBO.builder()
                .questionId(questionPo.getQuestionId())
                .teacherId(questionPo.getTeacherId())
                .content(questionPo.getContent())
                .questionType(questionPo.getQuestionType())
                .difficulty(questionPo.getDifficulty())
                .knowledgeId(questionPo.getKnowledgeId())
                .examId(questionPo.getExamId())
                .referenceAnswer(questionPo.getReferenceAnswer())
                .scorePoints(questionPo.getScorePoints())
                .answer(questionPo.getAnswer())
                .createdAt(questionPo.getCreatedAt())
                .updatedAt(questionPo.getUpdatedAt())
                .build();
    }

    public QuestionPo toQuestion() {
        QuestionPo questionPo = new QuestionPo();
        questionPo.setQuestionId(questionId);
        questionPo.setTeacherId(teacherId);
        questionPo.setContent(content);
        questionPo.setQuestionType(questionType);
        questionPo.setDifficulty(difficulty);
        questionPo.setKnowledgeId(knowledgeId);
        questionPo.setExamId(examId);
        questionPo.setReferenceAnswer(referenceAnswer);
        questionPo.setScorePoints(scorePoints);
        questionPo.setAnswer(answer);
        questionPo.setCreatedAt(createdAt);
        questionPo.setUpdatedAt(updatedAt);
        return questionPo;
    }
} 