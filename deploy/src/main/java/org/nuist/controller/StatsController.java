package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    /**
     * 获取指定用户类型的统计摘要
     *
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
        if (!Arrays.asList("TEACHER", "STUDENT","ALL").contains(userType.toUpperCase())) {
            throw new IllegalArgumentException("无效的用户类型，必须是 TEACHER 或 STUDENT 或 ALL");
        }

        if (!Arrays.asList("daily", "weekly").contains(period.toLowerCase())) {
            throw new IllegalArgumentException("无效的统计周期，必须是 daily 或 weekly");
        }

        // 根据周期类型处理
        String pattern;
        if ("daily".equalsIgnoreCase(period)) {
            if (date == null) date = LocalDate.now();
            pattern = "stats:daily:" + date + ":" + userType + ":*";
            return collectStatsByPattern(pattern);
        } else {
            if (weekId == null) {
                weekId = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-'W'ww"));
            }
            pattern = "stats:weekly:" + weekId + ":" + userType + ":*";
            return collectStatsByPattern(pattern);
        }
    }

    /**
     * 获取所有板块总使用次数（跨用户类型）
     *
     *
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
            if (weekId == null) {
                weekId = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-'W'ww"));
            }
            pattern = "stats:weekly:" + weekId + ":*";
        }
        period ="weekly";
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
     *
     * @param pattern Redis键匹配模式
     */
    private StatSummary collectStatsByPattern(String pattern) {
        // 获取匹配键列表
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

            // 解析模块名称（键的最后一部分）
            String module = extractModuleFromKey(key);
            totalUsage += count;

            // 统计各模块使用次数
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
        // 键格式：stats:[period]:[date/weekId]:[userType]:[module]
        String[] parts = key.split(":");
        if (parts.length >= 5) {
            return parts[parts.length - 1];
        }
        return "未知模块";
    }
}