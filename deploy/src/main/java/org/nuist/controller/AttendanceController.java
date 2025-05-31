package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.nuist.bo.AttendanceBO;
import org.nuist.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考勤管理控制器
 */
@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/attendance")
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    /**
     * 获取学生考勤记录
     * @param studentId 学生ID
     * @return 考勤记录列表
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceBO>> getStudentAttendance(@PathVariable("studentId") Long studentId) {
        List<AttendanceBO> attendanceList = attendanceService.getStudentAttendance(studentId);
        return ResponseEntity.ok(attendanceList);
    }
    
    /**
     * 获取学生某课程的考勤记录
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 考勤记录列表
     */
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<AttendanceBO>> getStudentCourseAttendance(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId) {
        List<AttendanceBO> attendanceList = attendanceService.getStudentCourseAttendance(studentId, courseId);
        return ResponseEntity.ok(attendanceList);
    }
    
    /**
     * 根据课程名称获取学生考勤记录
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 考勤记录列表
     */
    @GetMapping("/student/{studentId}/course/name")
    public ResponseEntity<List<AttendanceBO>> getStudentAttendanceByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("courseName") String courseName) {
        List<AttendanceBO> attendanceList = attendanceService.getStudentAttendanceByCourseName(studentId, courseName);
        return ResponseEntity.ok(attendanceList);
    }
    
    /**
     * 根据日期范围获取学生考勤记录
     * @param studentId 学生ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     */
    @GetMapping("/student/{studentId}/date-range")
    public ResponseEntity<List<AttendanceBO>> getStudentAttendanceByDateRange(
            @PathVariable("studentId") Long studentId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AttendanceBO> attendanceList = attendanceService.getStudentAttendanceByDateRange(studentId, startDate, endDate);
        return ResponseEntity.ok(attendanceList);
    }
    
    /**
     * 根据考勤状态获取学生考勤记录
     * @param studentId 学生ID
     * @param status 考勤状态
     * @return 考勤记录列表
     */
    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<AttendanceBO>> getStudentAttendanceByStatus(
            @PathVariable("studentId") Long studentId,
            @PathVariable("status") String status) {
        List<AttendanceBO> attendanceList = attendanceService.getStudentAttendanceByStatus(studentId, status);
        return ResponseEntity.ok(attendanceList);
    }
    
    /**
     * 保存考勤记录
     * @param attendance 考勤记录
     * @return 保存结果
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveAttendance(@RequestBody AttendanceBO attendance) {
        Long attendanceId = attendanceService.saveAttendance(attendance);
        
        Map<String, Object> result = new HashMap<>();
        if (attendanceId != null) {
            result.put("success", true);
            result.put("attendanceId", attendanceId);
            result.put("message", "考勤记录保存成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "考勤记录保存失败");
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    /**
     * 批量保存考勤记录
     * @param attendanceList 考勤记录列表
     * @return 保存结果
     */
    @PostMapping("/batch-save")
    public ResponseEntity<Map<String, Object>> batchSaveAttendance(@RequestBody List<AttendanceBO> attendanceList) {
        int count = attendanceService.batchSaveAttendance(attendanceList);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", count > 0);
        result.put("count", count);
        result.put("message", count > 0 ? "批量保存成功" : "批量保存失败");
        return ResponseEntity.ok(result);
    }
    
    /**
     * 更新考勤状态
     * @param attendanceId 考勤ID
     * @param status 考勤状态
     * @param remark 备注
     * @return 更新结果
     */
    @PutMapping("/{attendanceId}/status")
    public ResponseEntity<Map<String, Object>> updateAttendanceStatus(
            @PathVariable("attendanceId") Long attendanceId,
            @RequestParam("status") String status,
            @RequestParam(value = "remark", required = false) String remark) {
        boolean success = attendanceService.updateAttendanceStatus(attendanceId, status, remark);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "状态更新成功" : "状态更新失败");
        return ResponseEntity.ok(result);
    }
    
    /**
     * 根据课程名称更新考勤
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @param date 日期
     * @param status 考勤状态
     * @param remark 备注
     * @return 更新结果
     */
    @PutMapping("/student/{studentId}/course/update")
    public ResponseEntity<Map<String, Object>> updateAttendanceByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("courseName") String courseName,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("status") String status,
            @RequestParam(value = "remark", required = false) String remark) {
        boolean success = attendanceService.updateAttendanceByCourseName(studentId, courseName, date, status, remark);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "考勤更新成功" : "考勤更新失败");
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取学生考勤统计
     * @param studentId 学生ID
     * @return 考勤统计
     */
    @GetMapping("/student/{studentId}/statistics")
    public ResponseEntity<Map<String, Object>> getAttendanceStatistics(
            @PathVariable("studentId") Long studentId) {
        Map<String, Object> statistics = attendanceService.getAttendanceStatistics(studentId);
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * 获取学生特定课程的考勤统计
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 考勤统计
     */
    @GetMapping("/student/{studentId}/course/{courseId}/statistics")
    public ResponseEntity<Map<String, Object>> getCourseAttendanceStatistics(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId) {
        Map<String, Object> statistics = attendanceService.getCourseAttendanceStatistics(studentId, courseId);
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * 根据课程名称获取考勤统计
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 考勤统计
     */
    @GetMapping("/student/{studentId}/course/statistics")
    public ResponseEntity<Map<String, Object>> getAttendanceStatisticsByCourseName(
            @PathVariable("studentId") Long studentId,
            @RequestParam("courseName") String courseName) {
        Map<String, Object> statistics = attendanceService.getAttendanceStatisticsByCourseName(studentId, courseName);
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * 搜索考勤记录
     * @param studentId 学生ID
     * @param keywords 关键词
     * @param status 考勤状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     */
    @GetMapping("/student/{studentId}/search")
    public ResponseEntity<List<AttendanceBO>> searchAttendance(
            @PathVariable("studentId") Long studentId,
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AttendanceBO> attendanceList = attendanceService.searchAttendance(studentId, keywords, status, startDate, endDate);
        return ResponseEntity.ok(attendanceList);
    }
} 