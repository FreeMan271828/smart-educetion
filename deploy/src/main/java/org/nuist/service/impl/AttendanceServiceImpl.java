package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nuist.bo.AttendanceBO;
import org.nuist.mapper.AttendanceMapper;
import org.nuist.mapper.CourseMapper;
import org.nuist.po.AttendancePO;
import org.nuist.po.CoursePO;
import org.nuist.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考勤服务实现类
 */
@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, AttendancePO> implements AttendanceService{

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private CourseMapper courseMapper;
    
    @Override
    public List<AttendanceBO> getStudentAttendance(Long studentId) {
        if (studentId == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<AttendancePO> queryWrapper = Wrappers.<AttendancePO>lambdaQuery()
                .eq(AttendancePO::getStudentId, studentId)
                .orderByDesc(AttendancePO::getAttendanceDate);
        
        List<AttendancePO> poList = list(queryWrapper);
        return convertToBOList(poList);
    }

    @Override
    public List<AttendanceBO> getCourseAttendance(Long courseId) {
        if (courseId == null) {
            return new ArrayList<>();
        }
        return convertToBOList(list(
                Wrappers.<AttendancePO>lambdaQuery()
                        .eq(AttendancePO::getCourseId, courseId)
        ));
    }
    
    @Override
    public List<AttendanceBO> getStudentCourseAttendance(Long studentId, Long courseId) {
        if (studentId == null || courseId == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<AttendancePO> queryWrapper = Wrappers.<AttendancePO>lambdaQuery()
                .eq(AttendancePO::getStudentId, studentId)
                .eq(AttendancePO::getCourseId, courseId)
                .orderByDesc(AttendancePO::getAttendanceDate);
        
        List<AttendancePO> poList = list(queryWrapper);
        return convertToBOList(poList);
    }
    
    @Override
    public List<AttendanceBO> getStudentAttendanceByCourseName(Long studentId, String courseName) {
        if (studentId == null || !StringUtils.hasText(courseName)) {
            return new ArrayList<>();
        }
        
        // 使用MyBatis-Plus根据课程名称查询课程
        LambdaQueryWrapper<CoursePO> courseWrapper = Wrappers.<CoursePO>lambdaQuery()
                .eq(CoursePO::getName, courseName);
        CoursePO coursePO = courseMapper.selectOne(courseWrapper);
        
        if (coursePO == null) {
            return new ArrayList<>();
        }
        
        return getStudentCourseAttendance(studentId, coursePO.getId());
    }
    
    @Override
    public List<AttendanceBO> getStudentAttendanceByDateRange(Long studentId, LocalDate startDate, LocalDate endDate) {
        if (studentId == null || startDate == null || endDate == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<AttendancePO> queryWrapper = Wrappers.<AttendancePO>lambdaQuery()
                .eq(AttendancePO::getStudentId, studentId)
                .ge(AttendancePO::getAttendanceDate, startDate)
                .le(AttendancePO::getAttendanceDate, endDate)
                .orderByDesc(AttendancePO::getAttendanceDate);
        
        List<AttendancePO> poList = list(queryWrapper);
        return convertToBOList(poList);
    }
    
    @Override
    public List<AttendanceBO> getStudentAttendanceByStatus(Long studentId, String status) {
        if (studentId == null || !StringUtils.hasText(status)) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<AttendancePO> queryWrapper = Wrappers.<AttendancePO>lambdaQuery()
                .eq(AttendancePO::getStudentId, studentId)
                .eq(AttendancePO::getStatus, status)
                .orderByDesc(AttendancePO::getAttendanceDate);
        
        List<AttendancePO> poList = list(queryWrapper);
        return convertToBOList(poList);
    }
    
    @Override
    public Long saveAttendance(AttendanceBO attendanceBO) {
        if (attendanceBO == null) {
            return null;
        }
        
        AttendancePO po = attendanceBO.toPO();
        LocalDateTime now = LocalDateTime.now();
        
        if (po.getAttendanceId() == null) {
            // 新增
            po.setCreatedAt(now);
            po.setUpdatedAt(now);
            save(po);
        } else {
            // 更新
            po.setUpdatedAt(now);
            updateById(po);
        }
        
        return po.getAttendanceId();
    }
    
    @Override
    public int batchSaveAttendance(List<AttendanceBO> attendanceList) {
        if (attendanceList == null || attendanceList.isEmpty()) {
            return 0;
        }
        
        List<AttendancePO> poList = attendanceList.stream()
                .map(AttendanceBO::toPO)
                .collect(Collectors.toList());
        
        LocalDateTime now = LocalDateTime.now();
        for (AttendancePO po : poList) {
            if (po.getCreatedAt() == null) {
                po.setCreatedAt(now);
            }
            po.setUpdatedAt(now);
        }
        
        // MyBatis-Plus批量保存
        saveBatch(poList);
        
        return poList.size();
    }
    
    @Override
    public boolean updateAttendanceStatus(Long attendanceId, String status, String remark) {
        if (attendanceId == null || !StringUtils.hasText(status)) {
            return false;
        }
        
        AttendancePO po = getById(attendanceId);
        if (po == null) {
            return false;
        }
        
        po.setStatus(status);
        if (StringUtils.hasText(remark)) {
            po.setRemark(remark);
        }
        po.setUpdatedAt(LocalDateTime.now());
        
        return updateById(po);
    }
    
    @Override
    public boolean updateAttendanceByCourseName(Long studentId, String courseName, LocalDate date, String status, String remark) {
        if (studentId == null || !StringUtils.hasText(courseName) || date == null || !StringUtils.hasText(status)) {
            return false;
        }
        
        // 使用MyBatis-Plus根据课程名称查询课程
        LambdaQueryWrapper<CoursePO> courseWrapper = Wrappers.<CoursePO>lambdaQuery()
                .eq(CoursePO::getName, courseName);
        CoursePO coursePO = courseMapper.selectOne(courseWrapper);
        
        if (coursePO == null) {
            return false;
        }
        
        Long courseId = coursePO.getId();
        
        // 查询对应的考勤记录
        LambdaQueryWrapper<AttendancePO> queryWrapper = Wrappers.<AttendancePO>lambdaQuery()
                .eq(AttendancePO::getStudentId, studentId)
                .eq(AttendancePO::getCourseId, courseId)
                .eq(AttendancePO::getAttendanceDate, date);
        
        AttendancePO po = getOne(queryWrapper);
        
        if (po == null) {
            // 不存在考勤记录，创建新记录
            po = new AttendancePO();
            po.setStudentId(studentId);
            po.setCourseId(courseId);
            po.setAttendanceDate(date);
            po.setStatus(status);
            po.setRemark(remark);
            po.setRecordedBy(0L); // 假设系统自动记录
            LocalDateTime now = LocalDateTime.now();
            po.setCreatedAt(now);
            po.setUpdatedAt(now);
            return save(po);
        } else {
            // 更新现有记录
            po.setStatus(status);
            if (StringUtils.hasText(remark)) {
                po.setRemark(remark);
            }
            po.setUpdatedAt(LocalDateTime.now());
            return updateById(po);
        }
    }
    
    @Override
    public Map<String, Object> getAttendanceStatistics(Long studentId) {
        if (studentId == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> result = new HashMap<>();
        
        // 查询该学生的所有考勤记录
        LambdaQueryWrapper<AttendancePO> queryWrapper = Wrappers.<AttendancePO>lambdaQuery()
                .eq(AttendancePO::getStudentId, studentId);
        
        List<AttendancePO> attendanceList = list(queryWrapper);
        
        if (attendanceList.isEmpty()) {
            result.put("totalCount", 0);
            result.put("presentCount", 0);
            result.put("absentCount", 0);
            result.put("lateCount", 0);
            result.put("attendanceRate", 0);
            return result;
        }
        
        int totalCount = attendanceList.size();
        int presentCount = 0;
        int absentCount = 0;
        int lateCount = 0;
        
        for (AttendancePO po : attendanceList) {
            switch (po.getStatus()) {
                case AttendanceBO.STATUS_PRESENT:
                    presentCount++;
                    break;
                case AttendanceBO.STATUS_ABSENT:
                    absentCount++;
                    break;
                case AttendanceBO.STATUS_LATE:
                    lateCount++;
                    break;
            }
        }
        
        double attendanceRate = (double) (presentCount + lateCount) / totalCount * 100;
        
        result.put("totalCount", totalCount);
        result.put("presentCount", presentCount);
        result.put("absentCount", absentCount);
        result.put("lateCount", lateCount);
        result.put("attendanceRate", attendanceRate);
        
        return result;
    }
    
    @Override
    public Map<String, Object> getCourseAttendanceStatistics(Long studentId, Long courseId) {
        if (studentId == null || courseId == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> result = new HashMap<>();
        
        // 查询该学生在特定课程的考勤记录
        LambdaQueryWrapper<AttendancePO> queryWrapper = Wrappers.<AttendancePO>lambdaQuery()
                .eq(AttendancePO::getStudentId, studentId)
                .eq(AttendancePO::getCourseId, courseId);
        
        List<AttendancePO> attendanceList = list(queryWrapper);
        
        // 使用MyBatis-Plus根据ID查询课程名称
        CoursePO coursePO = courseMapper.selectById(courseId);
        String courseName = coursePO != null ? coursePO.getName() : "";
        
        if (attendanceList.isEmpty()) {
            result.put("courseId", courseId);
            result.put("courseName", courseName);
            result.put("totalCount", 0);
            result.put("presentCount", 0);
            result.put("absentCount", 0);
            result.put("lateCount", 0);
            result.put("attendanceRate", 0);
            return result;
        }
        
        int totalCount = attendanceList.size();
        int presentCount = 0;
        int absentCount = 0;
        int lateCount = 0;
        
        for (AttendancePO po : attendanceList) {
            switch (po.getStatus()) {
                case AttendanceBO.STATUS_PRESENT:
                    presentCount++;
                    break;
                case AttendanceBO.STATUS_ABSENT:
                    absentCount++;
                    break;
                case AttendanceBO.STATUS_LATE:
                    lateCount++;
                    break;
            }
        }
        
        double attendanceRate = (double) (presentCount + lateCount) / totalCount * 100;
        
        result.put("courseId", courseId);
        result.put("courseName", courseName);
        result.put("totalCount", totalCount);
        result.put("presentCount", presentCount);
        result.put("absentCount", absentCount);
        result.put("lateCount", lateCount);
        result.put("attendanceRate", attendanceRate);
        
        return result;
    }
    
    @Override
    public Map<String, Object> getAttendanceStatisticsByCourseName(Long studentId, String courseName) {
        if (studentId == null || !StringUtils.hasText(courseName)) {
            return new HashMap<>();
        }
        
        // 使用MyBatis-Plus根据课程名称查询课程
        LambdaQueryWrapper<CoursePO> courseWrapper = Wrappers.<CoursePO>lambdaQuery()
                .eq(CoursePO::getName, courseName);
        CoursePO coursePO = courseMapper.selectOne(courseWrapper);
        
        if (coursePO == null) {
            return new HashMap<>();
        }
        
        return getCourseAttendanceStatistics(studentId, coursePO.getId());
    }
    
    @Override
    public List<AttendanceBO> searchAttendance(Long studentId, String keywords, String status, LocalDate startDate, LocalDate endDate) {
        if (studentId == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<AttendancePO> queryWrapper = Wrappers.<AttendancePO>lambdaQuery()
                .eq(AttendancePO::getStudentId, studentId);
        
        // 添加关键词搜索条件（搜索备注字段）
        if (StringUtils.hasText(keywords)) {
            queryWrapper.like(AttendancePO::getRemark, keywords);
        }
        
        // 添加状态过滤条件
        if (StringUtils.hasText(status)) {
            queryWrapper.eq(AttendancePO::getStatus, status);
        }
        
        // 添加日期范围过滤条件
        if (startDate != null) {
            queryWrapper.ge(AttendancePO::getAttendanceDate, startDate);
        }
        
        if (endDate != null) {
            queryWrapper.le(AttendancePO::getAttendanceDate, endDate);
        }
        
        // 按日期降序排序
        queryWrapper.orderByDesc(AttendancePO::getAttendanceDate);
        
        List<AttendancePO> poList = list(queryWrapper);
        return convertToBOList(poList);
    }
    
    /**
     * 将PO列表转换为BO列表
     * @param poList PO列表
     * @return BO列表
     */
    private List<AttendanceBO> convertToBOList(List<AttendancePO> poList) {
        if (poList == null || poList.isEmpty()) {
            return new ArrayList<>();
        }
        
        return poList.stream()
                .map(AttendanceBO::fromPO)
                .collect(Collectors.toList());
    }
} 