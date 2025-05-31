package org.nuist.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 题目类型枚举
 */
@Getter
public enum QuestionType {
    
    CHOICE("CHOICE", "选择题"),
    JUDGMENT("JUDGMENT", "判断题"),
    FILL_BLANK("FILL_BLANK", "填空题"),
    SHORT_ANSWER("SHORT_ANSWER", "简答题");
    
    @EnumValue
    @JsonValue
    private final String code;
    
    private final String description;
    
    QuestionType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 根据编码获取枚举实例
     * @param code 编码
     * @return 枚举实例
     */
    public static QuestionType getByCode(String code) {
        for (QuestionType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的题目类型编码: " + code);
    }
}