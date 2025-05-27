package org.nuist.client;

import org.nuist.bo.AttendanceBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 考勤管理feign客户端
 */
@FeignClient(value = "classService")
public interface AttendanceClient {
    
    /**
     * 获取学生考勤记录
     * @param studentId 学生ID
     * @return 考勤记录列表
     */
    @GetMapping("/api/attendance/student/{studentId}")
    ResponseEntity<List<AttendanceBO>> getStudentAttendance(@PathVariable("studentId") Long studentId);
    
    /**
     * 获取学生某课程的考勤记录
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 考勤记录列表
     */
    @GetMapping("/api/attendance/student/{studentId}/course/{courseId}")
    ResponseEntity<List<AttendanceBO>> getStudentCourseAttendance(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId);
    
    /**
     * 根据课程名称获取学生考勤记录
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 考勤记录列表
     */
    @GetMapping("/api/attendance/student/{studentId}/course/name")
    ResponseEntity<List<AttendanceBO>> getStudentAttendanceByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("courseName") String courseName);
    
    /**
     * 根据日期范围获取学生考勤记录
     * @param studentId 学生ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     */
    @GetMapping("/api/attendance/student/{studentId}/date-range")
    ResponseEntity<List<AttendanceBO>> getStudentAttendanceByDateRange(
            @PathVariable("studentId") Long studentId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
    
    /**
     * 根据考勤状态获取学生考勤记录
     * @param studentId 学生ID
     * @param status 考勤状态
     * @return 考勤记录列表
     */
    @GetMapping("/api/attendance/student/{studentId}/status/{status}")
    ResponseEntity<List<AttendanceBO>> getStudentAttendanceByStatus(
            @PathVariable("studentId") Long studentId,
            @PathVariable("status") String status);
    
    /**
     * 保存考勤记录
     * @param attendance 考勤记录
     * @return 保存结果
     */
    @PostMapping("/api/attendance/save")
    ResponseEntity<Map<String, Object>> saveAttendance(@RequestBody AttendanceBO attendance);
    
    /**
     * 批量保存考勤记录
     * @param attendanceList 考勤记录列表
     * @return 保存结果
     */
    @PostMapping("/api/attendance/batch-save")
    ResponseEntity<Map<String, Object>> batchSaveAttendance(@RequestBody List<AttendanceBO> attendanceList);
    
    /**
     * 更新考勤状态
     * @param attendanceId 考勤ID
     * @param status 考勤状态
     * @param remark 备注
     * @return 更新结果
     */
    @PutMapping("/api/attendance/{attendanceId}/status")
    ResponseEntity<Map<String, Object>> updateAttendanceStatus(
            @PathVariable("attendanceId") Long attendanceId,
            @RequestParam("status") String status,
            @RequestParam(value = "remark", required = false) String remark);
    
    /**
     * 根据课程名称更新考勤
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @param date 日期
     * @param status 考勤状态
     * @param remark 备注
     * @return 更新结果
     */
    @PutMapping("/api/attendance/student/{studentId}/course/update")
    ResponseEntity<Map<String, Object>> updateAttendanceByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("courseName") String courseName,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("status") String status,
            @RequestParam(value = "remark", required = false) String remark);
    
    /**
     * 获取学生考勤统计
     * @param studentId 学生ID
     * @return 考勤统计
     */
    @GetMapping("/api/attendance/student/{studentId}/statistics")
    ResponseEntity<Map<String, Object>> getAttendanceStatistics(
            @PathVariable("studentId") Long studentId);
    
    /**
     * 获取学生特定课程的考勤统计
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 考勤统计
     */
    @GetMapping("/api/attendance/student/{studentId}/course/{courseId}/statistics")
    ResponseEntity<Map<String, Object>> getCourseAttendanceStatistics(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId);
    
    /**
     * 根据课程名称获取考勤统计
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 考勤统计
     */
    @GetMapping("/api/attendance/student/{studentId}/course/statistics")
    ResponseEntity<Map<String, Object>> getAttendanceStatisticsByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("courseName") String courseName);
    
    /**
     * 搜索考勤记录
     * @param studentId 学生ID
     * @param keywords 关键词
     * @param status 考勤状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     */
    @GetMapping("/api/attendance/student/{studentId}/search")
    ResponseEntity<List<AttendanceBO>> searchAttendance(
            @PathVariable("studentId") Long studentId,
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
}