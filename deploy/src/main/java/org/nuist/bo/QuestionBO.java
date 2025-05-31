package org.nuist.bo;

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
    private Long knowledgeId;
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
        questionPo.setReferenceAnswer(referenceAnswer);
        questionPo.setScorePoints(scorePoints);
        questionPo.setAnswer(answer);
        questionPo.setCreatedAt(createdAt);
        questionPo.setUpdatedAt(updatedAt);
        return questionPo;
    }
} 