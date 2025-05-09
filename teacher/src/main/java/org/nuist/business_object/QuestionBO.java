package org.nuist.business_object;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nuist.model.Question;

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
    private Integer scorePoints;
    private String answer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static QuestionBO fromQuestion(Question question) {
        return QuestionBO.builder()
                .questionId(question.getQuestionId())
                .teacherId(question.getTeacherId())
                .content(question.getContent())
                .questionType(question.getQuestionType())
                .difficulty(question.getDifficulty())
                .knowledgeId(question.getKnowledgeId())
                .referenceAnswer(question.getReferenceAnswer())
                .scorePoints(question.getScorePoints())
                .answer(question.getAnswer())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }

    public Question toQuestion() {
        Question question = new Question();
        question.setQuestionId(questionId);
        question.setTeacherId(teacherId);
        question.setContent(content);
        question.setQuestionType(questionType);
        question.setDifficulty(difficulty);
        question.setKnowledgeId(knowledgeId);
        question.setReferenceAnswer(referenceAnswer);
        question.setScorePoints(scorePoints);
        question.setAnswer(answer);
        question.setCreatedAt(createdAt);
        question.setUpdatedAt(updatedAt);
        return question;
    }
}