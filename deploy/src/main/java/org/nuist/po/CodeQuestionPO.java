package org.nuist.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.nuist.handlers.JsonbListTypeHandler;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "code_question", autoResultMap = true)
public class CodeQuestionPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属Exam
     */
    private Long examId;

    /**
     * 编程题标题
     */
    private String title;

    /**
     * 编程题详情，包括题目描述、样例输入输出和备注
     */
    private String description;

    /**
     * 题目分数
     */
    private Integer scorePoints;

    /**
     * JSON格式字符串（预期string[]）：用于题目展示的样例输入
     */
    @TableField(typeHandler = JsonbListTypeHandler.class, jdbcType = JdbcType.OTHER)
    private List<String> sampleInputs;

    /**
     * JSON格式字符串（预期string[]）：用于题目展示的样例输出
     */
    @TableField(typeHandler = JsonbListTypeHandler.class, jdbcType = JdbcType.OTHER)
    private List<String> sampleOutputs;

    /**
     * JSON格式字符串（预期string[]）：评测测试用例输入
     */
    @TableField(typeHandler = JsonbListTypeHandler.class, jdbcType = JdbcType.OTHER)
    private List<String> caseInputs;

    /**
     * JSON格式字符串（预期string[]）：评测测试用例预期输出
     */
    @TableField(typeHandler = JsonbListTypeHandler.class, jdbcType = JdbcType.OTHER)
    private List<String> caseOutputs;

    /**
     * 参考答案和解析
     */
    private String referenceAnswer;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
