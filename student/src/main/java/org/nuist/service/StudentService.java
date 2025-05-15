package org.nuist.service;

import org.nuist.business_object.StudentBO;
import org.nuist.dto.CreateStudentDTO;
import org.nuist.entity.TokenResponse;

import java.util.List;

/**
 * 学生服务接口
 */
public interface StudentService {
    
    /**
     * 根据ID获取学生信息
     * @param studentId 学生ID
     * @return 学生业务对象
     */
    StudentBO getStudentById(Long studentId);
    
    /**
     * 根据用户名获取学生信息
     * @param username 用户名
     * @return 学生业务对象
     */
    StudentBO getStudentByUsername(String username);
    
    /**
     * 根据邮箱获取学生信息
     * @param email 邮箱
     * @return 学生业务对象
     */
    StudentBO getStudentByEmail(String email);
    
    /**
     * 根据姓名模糊查询学生信息
     * @param fullName 学生姓名
     * @return 学生列表
     */
    List<StudentBO> getStudentsByFullName(String fullName);
    
    /**
     * 保存或更新学生信息
     * @param studentBO 学生业务对象
     * @return 保存后的学生ID
     */
    Long saveOrUpdateStudent(StudentBO studentBO);

    /**
     * 新学生注册
     * @param createStudentDTO 注册必要信息
     * @return JWT token
     */
    TokenResponse registerStudent(CreateStudentDTO createStudentDTO);
    
    /**
     * 根据班级和年级获取学生列表
     * @param grade 年级
     * @param className 班级
     * @return 学生列表
     */
    List<StudentBO> getStudentsByClass(String grade, String className);
    
    /**
     * 学生登录
     * @param username 用户名
     * @param password 密码
     * @return 学生业务对象，登录失败返回null
     */
    StudentBO login(String username, String password);
    
    /**
     * 修改密码
     * @param studentId 学生ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean changePassword(Long studentId, String oldPassword, String newPassword);
    
    /**
     * 多条件模糊查询学生
     * @param keywords 关键词(可匹配姓名、用户名、邮箱等)
     * @param grade 年级(可选)
     * @param className 班级(可选)
     * @return 学生列表
     */
    List<StudentBO> searchStudents(String keywords, String grade, String className);
    
    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean isUsernameExist(String username);
    
    /**
     * 检查邮箱是否已存在
     * @param email 邮箱
     * @return 是否存在
     */
    boolean isEmailExist(String email);
} 