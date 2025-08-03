package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("code_question_answer")
public class CodeQuestionAnswerPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 编程题目ID
     */
    private Long codeQuestionId;

    /**
     * 作答学生ID
     */
    private Long studentId;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 学生提交代码内容
     */
    private String studentCode;

    /**
     * 通过的用例个数
     */
    private Integer caseAccepted;

    /**
     * 该题目在提交时的总用例个数
     */
    private Integer caseTotal;

    /**
     * 评测结果
     */
    private String status;

    /**
     * 代码运行时间
     */
    private Long timeMs;

    /**
     * 测评提交时间
     */
    private LocalDateTime submitTime;
}
