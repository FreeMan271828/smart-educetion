package org.nuist.constant;

public class AnswerStatus {
    private AnswerStatus() {}

    // 未提交
    public static final String NOT_SUBMITTED = "NOT_SUBMITTED";

    // 已提交
    public static final String SUBMITTED = "SUBMITTED";

    // 已批改
    public static final String GRADED = "GRADED";

    public static String[] values() {
        return new String[] {NOT_SUBMITTED, SUBMITTED, GRADED};
    }

    public static boolean isValid(String status) {
        return NOT_SUBMITTED.equals(status) ||
                SUBMITTED.equals(status) ||
                GRADED.equals(status);
    }
}
