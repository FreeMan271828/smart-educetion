package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("question")
@Data
public class Question {

    @TableId(type = IdType.AUTO)
    private Long questionId;

    /**
     * 关联的教师ID
     */
    private Long teacherId;

    /**
     * 问题内容
     */
    private String content;

    /**
     * 问题类型
     */
    private String questionType;

    /**
     * 问题难度
     */
    private String difficulty;

    /**
     * 所属的知识点ID
     */
    private Long knowledgeId;

    /**
     * 参考答案
     */
    private String referenceAnswer;

    /**
     * 分数
     */
    private Integer scorePoints;

    /**
     * 作答内容
     */
    private String answer;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
