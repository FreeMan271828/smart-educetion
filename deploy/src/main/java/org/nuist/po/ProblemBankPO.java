package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("problem_bank")
public class ProblemBankPO {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String origin;

    private String title;

    private String content;

    private String type;

    private String expectedAnswer;

    private Double score;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
