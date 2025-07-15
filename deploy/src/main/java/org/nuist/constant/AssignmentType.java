package org.nuist.constant;

public class AssignmentType {
    private AssignmentType() {} // 防止实例化

    // 学生上传类型
    public static final String STUDENT_UPLOAD = "STUDENT_UPLOAD";

    // 教师布置类型
    public static final String TEACHER_ASSIGNED = "TEACHER_ASSIGNED";

    /**
     * 获取所有作业类型的数组
     */
    public static String[] values() {
        return new String[] {STUDENT_UPLOAD, TEACHER_ASSIGNED};
    }

    /**
     * 验证是否为有效的作业类型
     */
    public static boolean isValid(String type) {
        return STUDENT_UPLOAD.equals(type) || TEACHER_ASSIGNED.equals(type);
    }
}
