package org.nuist.service;

import org.nuist.bo.AttendanceBO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 考勤服务接口
 */
public interface AttendanceService {
    
    /**
     * 获取学生的考勤记录
     * @param studentId 学生ID
     * @return 考勤记录列表
     */
    List<AttendanceBO> getStudentAttendance(Long studentId);

    /**
     * 查询课程中的所有考勤记录
     * @param courseId 课程ID
     * @return 考勤记录列表
     */
    List<AttendanceBO> getCourseAttendance(Long courseId);
    
    /**
     * 获取学生在特定课程的考勤记录
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 考勤记录列表
     */
    List<AttendanceBO> getStudentCourseAttendance(Long studentId, Long courseId);
    
    /**
     * 根据课程名称获取学生的考勤记录
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 考勤记录列表
     */
    List<AttendanceBO> getStudentAttendanceByCourseName(Long studentId, String courseName);
    
    /**
     * 根据日期范围获取学生的考勤记录
     * @param studentId 学生ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     */
    List<AttendanceBO> getStudentAttendanceByDateRange(Long studentId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取学生特定状态的考勤记录
     * @param studentId 学生ID
     * @param status 考勤状态
     * @return 考勤记录列表
     */
    List<AttendanceBO> getStudentAttendanceByStatus(Long studentId, String status);
    
    /**
     * 保存学生考勤记录
     * @param attendanceBO 考勤业务对象
     * @return 保存后的考勤ID
     */
    Long saveAttendance(AttendanceBO attendanceBO);
    
    /**
     * 批量保存考勤记录
     * @param attendanceList 考勤记录列表
     * @return 成功保存的数量
     */
    int batchSaveAttendance(List<AttendanceBO> attendanceList);
    
    /**
     * 更新考勤状态
     * @param attendanceId 考勤ID
     * @param status 新状态
     * @param remark 备注
     * @return 是否更新成功
     */
    boolean updateAttendanceStatus(Long attendanceId, String status, String remark);
    
    /**
     * 根据课程名称和日期更新考勤状态
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @param date 日期
     * @param status 状态
     * @param remark 备注
     * @return 是否更新成功
     */
    boolean updateAttendanceByCourseName(Long studentId, String courseName, LocalDate date, String status, String remark);
    
    /**
     * 获取学生的考勤统计
     * @param studentId 学生ID
     * @return 考勤统计(包括出勤率、缺勤次数、迟到次数等)
     */
    Map<String, Object> getAttendanceStatistics(Long studentId);
    
    /**
     * 获取学生在特定课程的考勤统计
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 考勤统计
     */
    Map<String, Object> getCourseAttendanceStatistics(Long studentId, Long courseId);
    
    /**
     * 根据课程名称获取考勤统计
     * @param studentId 学生ID
     * @param courseName 课程名称
     * @return 考勤统计
     */
    Map<String, Object> getAttendanceStatisticsByCourseName(Long studentId, String courseName);
    
    /**
     * 多条件搜索考勤记录
     * @param studentId 学生ID
     * @param keywords 关键词(可匹配课程名称、备注等)
     * @param status 状态(可选)
     * @param startDate 开始日期(可选)
     * @param endDate 结束日期(可选)
     * @return 考勤记录列表
     */
    List<AttendanceBO> searchAttendance(Long studentId, String keywords, String status, LocalDate startDate, LocalDate endDate);
} 