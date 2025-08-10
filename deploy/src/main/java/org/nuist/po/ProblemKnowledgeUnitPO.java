package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("problem_knowledge_unit")
public class ProblemKnowledgeUnitPO {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long problemId;

    private Long knowledgeUnitId;

    private Integer weight;

    private LocalDateTime createdAt;

}
