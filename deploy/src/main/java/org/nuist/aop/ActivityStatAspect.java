package org.nuist.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.time.LocalDate; // 使用LocalDate而不是LocalDateTime
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class ActivityStatAspect {

    private static final DateTimeFormatter WEEK_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-'W'ww");

    private static final String DAILY_KEY_PREFIX = "stats:daily:";
    private static final String WEEKLY_KEY_PREFIX = "stats:weekly:";


    @Autowired
    @Qualifier("longRedisTemplate")
    private RedisTemplate<String, Long> redisTemplate;



    // 修复1：方法参数声明修正
    @AfterReturning("@annotation(activityStat)")
    public void recordActivity(JoinPoint joinPoint, ActivityStat activityStat) {
        // 验证模块名称非空
        if (!StringUtils.hasText(activityStat.module())) {
            throw new IllegalStateException("ActivityStat注解必须指定模块名称");
        }

        // 修复2：正则表达式转义修正
        String cleanModule = activityStat.module()
                .replaceAll("[:\\\\s]", "_") // 正确的反斜杠转义
                .trim();

        // 修复3：使用LocalDate而不是LocalDateTime
        LocalDate today = LocalDate.now();

        // 修复4：修正incrementStat方法调用
        for (ActivityStat.StatType statType : activityStat.statTypes()) {
            switch (statType) {
                case DAILY:
                    incrementStat(DAILY_KEY_PREFIX, today, activityStat.userType(), cleanModule, 7, TimeUnit.DAYS);
                    break;
                case WEEKLY:
                    incrementStat(WEEKLY_KEY_PREFIX, today, activityStat.userType(), cleanModule, 21, TimeUnit.DAYS);
                    break;
            }
        }
    }

    private void incrementStat(String prefix, LocalDate date, ActivityStat.UserType userType,
                               String module, long duration, TimeUnit unit) {
        String key;
        if (prefix.equals(DAILY_KEY_PREFIX)) {
            key = DAILY_KEY_PREFIX + date.format(DateTimeFormatter.ISO_DATE) +
                    ":" + userType.name() + ":" + module;
        } else {
            key = WEEKLY_KEY_PREFIX + date.format(WEEK_FORMATTER) +
                    ":" + userType.name() + ":" + module;
        }

        // 原子递增并设置过期时间
        redisTemplate.opsForValue().increment(key, 1L);

        // 安全设置过期时间（避免重复设置）
        Long expire = redisTemplate.getExpire(key);
        if (expire == null || expire < 0) {
            redisTemplate.expire(key, duration, unit);
        }
    }
}