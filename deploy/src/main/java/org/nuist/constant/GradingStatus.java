package org.nuist.constant;

public class GradingStatus {
    private GradingStatus() {}

    // 待处理
    public static final String PENDING = "PENDING";

    // 处理中
    public static final String PROCESSING = "PROCESSING";

    // 成功
    public static final String SUCCESS = "SUCCESS";

    // 失败
    public static final String FAILED = "FAILED";

    public static String[] values() {
        return new String[] {PENDING, PROCESSING, SUCCESS, FAILED};
    }

    public static boolean isValid(String status) {
        return PENDING.equals(status) ||
                PROCESSING.equals(status) ||
                SUCCESS.equals(status) ||
                FAILED.equals(status);
    }
}
