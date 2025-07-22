package org.nuist.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActivityStat {
    /**
     * 功能模块名称（如"作业批改"、"测验作答"）
     */
    String module();

    /**
     * 用户类型（教师/学生）
     */
    UserType userType();

    /**
     * 统计类型（默认统计当日和本周）
     */
    StatType[] statTypes() default {StatType.DAILY, StatType.WEEKLY};

    enum UserType {
        TEACHER,
        STUDENT,
        ALL
    }

    enum StatType {
        DAILY,   // 当日统计
        WEEKLY   // 本周统计
    }
}
