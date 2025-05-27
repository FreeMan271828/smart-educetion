package org.nuist.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.nuist.po.StudentExamAnswerPO;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学生考试答案业务对象，用于业务逻辑处理
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentExamAnswerBO {
    
    private Long answerId;
    private Long examId;
    private Long questionId;
    private Long studentId;
    private String studentAnswer;
    private Double score;
    private String feedback;
    private Long gradedBy;
    private LocalDateTime gradedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 非数据库字段，用于业务扩展
    private String examTitle;
    private String questionContent;
    private String questionType;
    private String referenceAnswer;
    private String teacherName;
    
    /**
     * 将业务对象转换为持久化对象
     * @return 学生考试答案持久化对象
     */
    public StudentExamAnswerPO toPO() {
        StudentExamAnswerPO po = new StudentExamAnswerPO();
        po.setAnswerId(this.answerId);
        po.setExamId(this.examId);
        po.setQuestionId(this.questionId);
        po.setStudentId(this.studentId);
        po.setStudentAnswer(this.studentAnswer);
        po.setScore(this.score);
        po.setFeedback(this.feedback);
        po.setGradedBy(this.gradedBy);
        po.setGradedAt(this.gradedAt);
        po.setCreatedAt(this.createdAt);
        po.setUpdatedAt(this.updatedAt);
        return po;
    }
    
    /**
     * 将持久化对象转换为业务对象
     * @param po 学生考试答案持久化对象
     * @return 学生考试答案业务对象
     */
    public static StudentExamAnswerBO fromPO(StudentExamAnswerPO po) {
        return StudentExamAnswerBO.builder()
                .answerId(po.getAnswerId())
                .examId(po.getExamId())
                .questionId(po.getQuestionId())
                .studentId(po.getStudentId())
                .studentAnswer(po.getStudentAnswer())
                .score(po.getScore())
                .feedback(po.getFeedback())
                .gradedBy(po.getGradedBy())
                .gradedAt(po.getGradedAt())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }
    
    /**
     * 判断是否已经评分
     * @return 是否已评分
     */
    public boolean isGraded() {
        return this.score != null && this.gradedAt != null;
    }
    
    /**
     * 评分
     * @param score 分数
     * @param feedback 反馈
     * @param gradedBy 评分教师ID
     */
    public void grade(Double score, String feedback, Long gradedBy) {
        this.score = score;
        this.feedback = feedback;
        this.gradedBy = gradedBy;
        this.gradedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
} 