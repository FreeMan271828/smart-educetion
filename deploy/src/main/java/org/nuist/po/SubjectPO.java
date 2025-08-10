package org.nuist.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("subject")
public class SubjectPO {
    @TableId(type=com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Long id;

    private String name;

    private String description;

    private String grade;

    private Integer status;

    private String category;

    private Integer credit;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
