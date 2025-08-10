package org.nuist.bo;

import lombok.*;
import org.nuist.po.LearningPlanPO;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningPlanBO {

    private long planId;

    private long studentId;

    private String planName;

    private String content;

    private LocalDateTime beginAt;

    private LocalDateTime endAt;

    private boolean completed;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 新增检查方法
    @Getter
    private boolean completedSet = false;

    // 修改setter方法
    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.completedSet = true;  // 标记字段已被设置
    }


    public static LearningPlanBO fromLearningPlanPO(LearningPlanPO learningPlanPo) {
        return LearningPlanBO.builder()
                .planId(learningPlanPo.getPlanId())
                .studentId(learningPlanPo.getStudentId())
                .planName(learningPlanPo.getName())
                .content(learningPlanPo.getContent())
                .beginAt(learningPlanPo.getBeginAt())
                .endAt(learningPlanPo.getEndAt())
                .completed(learningPlanPo.isCompleted())
                .createdAt(learningPlanPo.getCreatedAt())
                .updatedAt(learningPlanPo.getUpdatedAt())
                .build();
    }


    public static LearningPlanPO toLearningPlanPO(LearningPlanBO learningPlanBO) {
        return LearningPlanPO.builder()
                .planId(learningPlanBO.getPlanId())
                .studentId(learningPlanBO.getStudentId())
                .name(learningPlanBO.getPlanName())
                .content(learningPlanBO.getContent())
                .beginAt(learningPlanBO.getBeginAt())
                .endAt(learningPlanBO.getEndAt())
                .completed(learningPlanBO.isCompleted())
                .createdAt(learningPlanBO.getCreatedAt())
                .updatedAt(learningPlanBO.getUpdatedAt())
                .build();
    }
}
