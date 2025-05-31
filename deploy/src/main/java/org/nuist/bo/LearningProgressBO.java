package org.nuist.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.nuist.po.LearningProgressPO;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学习进度业务对象，用于业务逻辑处理
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningProgressBO {
    
    private Long progressId;
    private Long studentId;
    private Long courseId;
    private Long knowledgeId;
    private BigDecimal masteryLevel;
    private String learningStatus;
    private LocalDateTime lastReviewedAt;
    private Integer reviewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 非数据库字段，用于业务扩展
    private String courseName;
    private String knowledgeName;
    
    /**
     * 将业务对象转换为持久化对象
     * @return 学习进度持久化对象
     */
    public LearningProgressPO toPO() {
        LearningProgressPO po = new LearningProgressPO();
        po.setProgressId(this.progressId);
        po.setStudentId(this.studentId);
        po.setCourseId(this.courseId);
        po.setKnowledgeId(this.knowledgeId);
        po.setMasteryLevel(this.masteryLevel);
        po.setLearningStatus(this.learningStatus);
        po.setLastReviewedAt(this.lastReviewedAt);
        po.setReviewCount(this.reviewCount);
        po.setCreatedAt(this.createdAt);
        po.setUpdatedAt(this.updatedAt);
        return po;
    }
    
    /**
     * 将持久化对象转换为业务对象
     * @param po 学习进度持久化对象
     * @return 学习进度业务对象
     */
    public static LearningProgressBO fromPO(LearningProgressPO po) {
        return LearningProgressBO.builder()
                .progressId(po.getProgressId())
                .studentId(po.getStudentId())
                .courseId(po.getCourseId())
                .knowledgeId(po.getKnowledgeId())
                .masteryLevel(po.getMasteryLevel())
                .learningStatus(po.getLearningStatus())
                .lastReviewedAt(po.getLastReviewedAt())
                .reviewCount(po.getReviewCount())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }
    
    /**
     * 判断是否已掌握
     * @return 是否已掌握
     */
    public boolean isMastered() {
        return this.masteryLevel != null && this.masteryLevel.compareTo(new BigDecimal("0.8")) >= 0;
    }
    
    /**
     * 更新掌握度
     * @param newScore 新得分
     */
    public void updateMasteryLevel(BigDecimal newScore) {
        if (this.masteryLevel == null) {
            this.masteryLevel = newScore;
        } else {
            // 计算新的掌握度（旧掌握度权重0.7，新得分权重0.3）
            this.masteryLevel = this.masteryLevel.multiply(new BigDecimal("0.7"))
                    .add(newScore.multiply(new BigDecimal("0.3")));
        }
        
        this.reviewCount = this.reviewCount == null ? 1 : this.reviewCount + 1;
        this.lastReviewedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
} 