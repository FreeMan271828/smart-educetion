package org.nuist.constant;

public class AssignmentStatus {
    private AssignmentStatus() {}

    // 草稿状态
    public static final String DRAFT = "DRAFT";

    // 已发布状态
    public static final String PUBLISHED = "PUBLISHED";

    // 已结束状态
    public static final String ENDED = "ENDED";

    public static String[] values() {
        return new String[] {DRAFT, PUBLISHED, ENDED};
    }

    public static boolean isValid(String status) {
        return DRAFT.equals(status) || PUBLISHED.equals(status) || ENDED.equals(status);
    }
}
