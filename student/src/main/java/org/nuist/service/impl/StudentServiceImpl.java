package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nuist.business_object.StudentBO;
import org.nuist.persist_object.StudentPO;
import org.nuist.dao.StudentMapper;
import org.nuist.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学生服务实现类
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, StudentPO> implements StudentService {
    
    @Override
    public StudentBO getStudentById(Long studentId) {
        if (studentId == null) {
            return null;
        }
        StudentPO studentPO = getById(studentId);
        return studentPO != null ? StudentBO.fromPO(studentPO) : null;
    }
    
    @Override
    public StudentBO getStudentByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        
        LambdaQueryWrapper<StudentPO> queryWrapper = Wrappers.<StudentPO>lambdaQuery()
                .eq(StudentPO::getUsername, username);
        
        StudentPO studentPO = getOne(queryWrapper);
        return studentPO != null ? StudentBO.fromPO(studentPO) : null;
    }
    
    @Override
    public StudentBO getStudentByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }
        
        LambdaQueryWrapper<StudentPO> queryWrapper = Wrappers.<StudentPO>lambdaQuery()
                .eq(StudentPO::getEmail, email);
        
        StudentPO studentPO = getOne(queryWrapper);
        return studentPO != null ? StudentBO.fromPO(studentPO) : null;
    }
    
    @Override
    public List<StudentBO> getStudentsByFullName(String fullName) {
        if (!StringUtils.hasText(fullName)) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<StudentPO> queryWrapper = Wrappers.<StudentPO>lambdaQuery()
                .like(StudentPO::getFullName, fullName);
        
        List<StudentPO> studentPOList = list(queryWrapper);
        return convertToBOList(studentPOList);
    }
    
    @Override
    public Long saveOrUpdateStudent(StudentBO studentBO) {
        if (studentBO == null) {
            return null;
        }
        
        StudentPO studentPO = studentBO.toPO();
        LocalDateTime now = LocalDateTime.now();
        
        if (studentPO.getStudentId() == null) {
            // 新增
            studentPO.setCreatedAt(now);
            studentPO.setUpdatedAt(now);
            save(studentPO);
        } else {
            // 更新
            studentPO.setUpdatedAt(now);
            updateById(studentPO);
        }
        
        return studentPO.getStudentId();
    }
    
    @Override
    public List<StudentBO> getStudentsByClass(String grade, String className) {
        LambdaQueryWrapper<StudentPO> queryWrapper = Wrappers.<StudentPO>lambdaQuery();
        
        if (StringUtils.hasText(grade)) {
            queryWrapper.eq(StudentPO::getGrade, grade);
        }
        
        if (StringUtils.hasText(className)) {
            queryWrapper.eq(StudentPO::getClassName, className);
        }
        
        if (!StringUtils.hasText(grade) && !StringUtils.hasText(className)) {
            return new ArrayList<>();
        }
        
        List<StudentPO> studentPOList = list(queryWrapper);
        return convertToBOList(studentPOList);
    }
    
    @Override
    public StudentBO login(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return null;
        }
        
        LambdaQueryWrapper<StudentPO> queryWrapper = Wrappers.<StudentPO>lambdaQuery()
                .eq(StudentPO::getUsername, username);
        
        StudentPO studentPO = getOne(queryWrapper);
        if (studentPO != null && password.equals(studentPO.getPassword())) {
            return StudentBO.fromPO(studentPO);
        }
        
        return null;
    }
    
    @Override
    public boolean changePassword(Long studentId, String oldPassword, String newPassword) {
        if (studentId == null || !StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
            return false;
        }
        
        StudentPO studentPO = getById(studentId);
        if (studentPO != null && oldPassword.equals(studentPO.getPassword())) {
            studentPO.setPassword(newPassword);
            studentPO.setUpdatedAt(LocalDateTime.now());
            return updateById(studentPO);
        }
        
        return false;
    }
    
    @Override
    public List<StudentBO> searchStudents(String keywords, String grade, String className) {
        LambdaQueryWrapper<StudentPO> queryWrapper = Wrappers.<StudentPO>lambdaQuery();
        
        if (StringUtils.hasText(keywords)) {
            queryWrapper.and(wrapper -> wrapper
                    .like(StudentPO::getFullName, keywords)
                    .or()
                    .like(StudentPO::getUsername, keywords)
                    .or()
                    .like(StudentPO::getEmail, keywords));
        }
        
        if (StringUtils.hasText(grade)) {
            queryWrapper.eq(StudentPO::getGrade, grade);
        }
        
        if (StringUtils.hasText(className)) {
            queryWrapper.eq(StudentPO::getClassName, className);
        }
        
        if (!StringUtils.hasText(keywords) && !StringUtils.hasText(grade) && !StringUtils.hasText(className)) {
            return new ArrayList<>();
        }
        
        List<StudentPO> studentPOList = list(queryWrapper);
        return convertToBOList(studentPOList);
    }
    
    @Override
    public boolean isUsernameExist(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        
        LambdaQueryWrapper<StudentPO> queryWrapper = Wrappers.<StudentPO>lambdaQuery()
                .eq(StudentPO::getUsername, username);
        
        return count(queryWrapper) > 0;
    }
    
    @Override
    public boolean isEmailExist(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        
        LambdaQueryWrapper<StudentPO> queryWrapper = Wrappers.<StudentPO>lambdaQuery()
                .eq(StudentPO::getEmail, email);
        
        return count(queryWrapper) > 0;
    }
    
    /**
     * 将PO列表转换为BO列表
     * @param poList PO列表
     * @return BO列表
     */
    private List<StudentBO> convertToBOList(List<StudentPO> poList) {
        if (poList == null || poList.isEmpty()) {
            return new ArrayList<>();
        }
        
        return poList.stream()
                .map(StudentBO::fromPO)
                .collect(Collectors.toList());
    }
} 