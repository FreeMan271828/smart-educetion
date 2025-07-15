package org.nuist.constant;

public class ProblemType {
    private ProblemType() {}

    // 单选题
    public static final String SINGLE_CHOICE = "SINGLE_CHOICE";

    // 多选题
    public static final String MULTI_CHOICE = "MULTI_CHOICE";

    // 填空题
    public static final String FILL_BLANK = "FILL_BLANK";

    // 简答题
    public static final String ESSAY_QUESTION = "ESSAY_QUESTION";

    // 判断题
    public static final String TRUE_FALSE = "TRUE_FALSE";

    public static String[] values() {
        return new String[] {SINGLE_CHOICE, MULTI_CHOICE, FILL_BLANK, ESSAY_QUESTION, TRUE_FALSE};
    }

    public static boolean isValid(String type) {
        return SINGLE_CHOICE.equals(type) ||
                MULTI_CHOICE.equals(type) ||
                FILL_BLANK.equals(type) ||
                ESSAY_QUESTION.equals(type) ||
                TRUE_FALSE.equals(type);
    }
}
