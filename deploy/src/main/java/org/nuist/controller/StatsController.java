package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    /**
     * 获取指定用户类型的统计摘要
     */
    @Operation(summary = "获取指定用户类型的统计摘要,userType只能为 TEACHER 或 STUDENT ,period只能为 daily 或 weekly")
    @GetMapping("/summary")
    public StatSummary getStatsSummary(
            @RequestParam String userType,
            @RequestParam String period,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam(required = false) String weekId) {

        // 校验参数
        if (!Arrays.asList("TEACHER", "STUDENT", "ALL").contains(userType.toUpperCase())) {
            throw new IllegalArgumentException("无效的用户类型，必须是 TEACHER 或 STUDENT 或 ALL");
        }

        if (!Arrays.asList("daily", "weekly").contains(period.toLowerCase())) {
            throw new IllegalArgumentException("无效的统计周期，必须是 daily 或 weekly");
        }

        // 根据周期类型处理
        if ("daily".equalsIgnoreCase(period)) {
            if (date == null) date = LocalDate.now();
            String pattern = "stats:daily:" + date + ":" + userType + ":*";
            return collectStatsByPattern(pattern);
        } else {
            // 获取本周起止日期（周一至周日）
            LocalDate[] weekRange = getWeekRange(weekId);
            LocalDate startDate = weekRange[0];
            LocalDate endDate = weekRange[1];

            // 收集本周所有日统计数据
            Map<String, Integer> mergedModuleStats = new LinkedHashMap<>();
            int totalUsage = 0;

            for (LocalDate day = startDate; !day.isAfter(endDate); day = day.plusDays(1)) {
                String dailyPattern = "stats:daily:" + day + ":" + userType + ":*";
                StatSummary dailyStats = collectStatsByPattern(dailyPattern);

                totalUsage += dailyStats.getTotalUsage();
                dailyStats.getModuleStats().forEach((module, count) ->
                        mergedModuleStats.merge(module, count, Integer::sum)
                );
            }

            return new StatSummary(totalUsage, mergedModuleStats);
        }
    }

    /**
     * 获取本周起止日期（周一至周日）
     * @param weekId 周ID（格式：yyyy-Www），如果为null则使用当前周
     */
    private LocalDate[] getWeekRange(String weekId) {
        LocalDate baseDate;

        if (weekId != null && !weekId.isEmpty()) {
            // 解析周ID格式：2023-W43
            int year = Integer.parseInt(weekId.substring(0, 4));
            int week = Integer.parseInt(weekId.substring(6));

            baseDate = LocalDate.of(year, 1, 1)
                    .with(TemporalAdjusters.firstDayOfYear())
                    .with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
                    .plusWeeks(week - 1);
        } else {
            baseDate = LocalDate.now();
        }

        LocalDate monday = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = baseDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return new LocalDate[]{monday, sunday};
    }

    /**
     * 获取所有板块总使用次数（跨用户类型）
     */
    @Operation(summary = "获取所有板块总使用次数（跨用户类型）period只能为 daily 或 weekly")
    @GetMapping("/total")
    public TotalUsageStats getTotalUsage(
            @RequestParam String period,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam(required = false) String weekId) {

        String pattern;
        if ("daily".equalsIgnoreCase(period)) {
            if (date == null) date = LocalDate.now();
            pattern = "stats:daily:" + date + ":*";
        } else {
            LocalDate[] weekRange = getWeekRange(weekId);
            LocalDate startDate = weekRange[0];
            LocalDate endDate = weekRange[1];

            int total = 0;
            for (LocalDate day = startDate; !day.isAfter(endDate); day = day.plusDays(1)) {
                String dailyPattern = "stats:daily:" + day + ":*";
                total += collectTotalStatsByPattern(dailyPattern);
            }
            return new TotalUsageStats(period, total);
        }
        return new TotalUsageStats(period, collectTotalStatsByPattern(pattern));
    }

    /**
     * 统计摘要对象
     */
    static class StatSummary {
        private int totalUsage;
        private Map<String, Integer> moduleStats = new LinkedHashMap<>();

        public StatSummary(int totalUsage, Map<String, Integer> moduleStats) {
            this.totalUsage = totalUsage;
            this.moduleStats = moduleStats;
        }

        public int getTotalUsage() {
            return totalUsage;
        }

        public Map<String, Integer> getModuleStats() {
            return moduleStats;
        }
    }

    /**
     * 总使用次数对象
     */
    static class TotalUsageStats {
        private String period;
        private int total;

        public TotalUsageStats(String period, int total) {
            this.period = period;
            this.total = total;
        }

        public String getPeriod() {
            return period;
        }

        public int getTotal() {
            return total;
        }
    }

    /**
     * 根据键模式收集统计信息
     */
    private StatSummary collectStatsByPattern(String pattern) {
        Set<String> keys = redisTemplate.execute((RedisCallback<Set<byte[]>>) connection ->
                        connection.keys(pattern.getBytes())
                ).stream()
                .map(String::new)
                .collect(Collectors.toSet());

        int totalUsage = 0;
        Map<String, Integer> moduleStats = new LinkedHashMap<>();

        for (String key : keys) {
            Long count = redisTemplate.opsForValue().get(key);
            if (count == null) continue;

            String module = extractModuleFromKey(key);
            totalUsage += count;

            moduleStats.put(module, moduleStats.getOrDefault(module, 0) + count.intValue());
        }

        return new StatSummary(totalUsage, moduleStats);
    }

    /**
     * 收集总使用次数（不区分模块）
     */
    private int collectTotalStatsByPattern(String pattern) {
        Set<String> keys = redisTemplate.execute((RedisCallback<Set<byte[]>>) connection ->
                        connection.keys(pattern.getBytes())
                ).stream()
                .map(String::new)
                .collect(Collectors.toSet());

        int total = 0;
        for (String key : keys) {
            Long count = redisTemplate.opsForValue().get(key);
            if (count != null) {
                total += count;
            }
        }
        return total;
    }

    /**
     * 从键名中提取模块名称
     */
    private String extractModuleFromKey(String key) {
        String[] parts = key.split(":");
        if (parts.length >= 5) {
            return parts[parts.length - 1];
        }
        return "未知模块";
    }
}