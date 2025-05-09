package org.nuist.business_object;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.nuist.persist_object.AttendancePO;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 考勤业务对象，用于业务逻辑处理
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceBO {
    
    private Long attendanceId;
    private Long courseId;
    private Long studentId;
    private String status;  // present, absent, late
    private LocalDate attendanceDate;
    private String remark;
    private Long recordedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 非数据库字段，用于业务扩展
    private String courseName;
    private String studentName;
    private String recorderName;
    
    // 常量定义
    public static final String STATUS_PRESENT = "present";
    public static final String STATUS_ABSENT = "absent";
    public static final String STATUS_LATE = "late";
    
    /**
     * 将业务对象转换为持久化对象
     * @return 考勤持久化对象
     */
    public AttendancePO toPO() {
        AttendancePO po = new AttendancePO();
        po.setAttendanceId(this.attendanceId);
        po.setCourseId(this.courseId);
        po.setStudentId(this.studentId);
        po.setStatus(this.status);
        po.setAttendanceDate(this.attendanceDate);
        po.setRemark(this.remark);
        po.setRecordedBy(this.recordedBy);
        po.setCreatedAt(this.createdAt);
        po.setUpdatedAt(this.updatedAt);
        return po;
    }
    
    /**
     * 将持久化对象转换为业务对象
     * @param po 考勤持久化对象
     * @return 考勤业务对象
     */
    public static AttendanceBO fromPO(AttendancePO po) {
        return AttendanceBO.builder()
                .attendanceId(po.getAttendanceId())
                .courseId(po.getCourseId())
                .studentId(po.getStudentId())
                .status(po.getStatus())
                .attendanceDate(po.getAttendanceDate())
                .remark(po.getRemark())
                .recordedBy(po.getRecordedBy())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }
    
    /**
     * 判断是否出勤
     * @return 是否出勤
     */
    public boolean isPresent() {
        return STATUS_PRESENT.equals(this.status);
    }
    
    /**
     * 判断是否缺勤
     * @return 是否缺勤
     */
    public boolean isAbsent() {
        return STATUS_ABSENT.equals(this.status);
    }
    
    /**
     * 判断是否迟到
     * @return 是否迟到
     */
    public boolean isLate() {
        return STATUS_LATE.equals(this.status);
    }
    
    /**
     * 更新考勤状态
     * @param status 新的考勤状态
     * @param remark 备注
     */
    public void updateStatus(String status, String remark) {
        this.status = status;
        this.remark = remark;
        this.updatedAt = LocalDateTime.now();
    }
} 