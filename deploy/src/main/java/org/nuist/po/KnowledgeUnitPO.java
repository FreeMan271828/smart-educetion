package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@TableName("knowledge_unit")
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeUnitPO {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String subject;

    private LocalDateTime createdAt;

    private Integer status;
}
