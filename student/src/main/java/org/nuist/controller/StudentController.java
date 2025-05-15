package org.nuist.controller;

import org.nuist.business_object.StudentBO;
import org.nuist.dto.CreateStudentDTO;
import org.nuist.entity.TokenResponse;
import org.nuist.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生信息管理控制器
 */
@RestController
@RequestMapping("/api/student")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    /**
     * 根据学生ID获取学生信息
     * @param studentId 学生ID
     * @return 学生信息
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentBO> getStudentById(@PathVariable("studentId") Long studentId) {
        StudentBO student = studentService.getStudentById(studentId);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据用户名获取学生信息
     * @param username 用户名
     * @return 学生信息
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<StudentBO> getStudentByUsername(@PathVariable("username") String username) {
        StudentBO student = studentService.getStudentByUsername(username);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据姓名模糊查询学生信息
     * @param fullName 姓名
     * @return 学生信息列表
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<StudentBO>> getStudentsByFullName(@RequestParam("fullName") String fullName) {
        List<StudentBO> students = studentService.getStudentsByFullName(fullName);
        return ResponseEntity.ok(students);
    }
    
    /**
     * 根据班级查询学生信息
     * @param grade 年级
     * @param className 班级名称
     * @return 学生信息列表
     */
    @GetMapping("/class")
    public ResponseEntity<List<StudentBO>> getStudentsByClass(
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "className", required = false) String className) {
        List<StudentBO> students = studentService.getStudentsByClass(grade, className);
        return ResponseEntity.ok(students);
    }
    
    /**
     * 综合搜索学生信息
     * @param keywords 关键词
     * @param grade 年级
     * @param className 班级名称
     * @return 学生信息列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<StudentBO>> searchStudents(
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "className", required = false) String className) {
        List<StudentBO> students = studentService.searchStudents(keywords, grade, className);
        return ResponseEntity.ok(students);
    }
    
    /**
     * 保存或更新学生信息
     * @param student 学生信息
     * @return 保存结果
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveOrUpdateStudent(@RequestBody StudentBO student) {
        Long studentId = studentService.saveOrUpdateStudent(student);
        
        Map<String, Object> result = new HashMap<>();
        if (studentId != null) {
            result.put("success", true);
            result.put("studentId", studentId);
            result.put("message", "学生信息保存成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "学生信息保存失败");
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 学生注册
     * @param dto 注册必要信息
     * @return 认证JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> registerStudent(@RequestBody CreateStudentDTO dto) {
        return ResponseEntity.ok(studentService.registerStudent(dto));
    }
    
//    /**
//     * 学生登录
//     * @param username 用户名
//     * @param password 密码
//     * @return 登录结果
//     */
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> login(
//            @RequestParam("username") String username,
//            @RequestParam("password") String password) {
//        StudentBO student = studentService.login(username, password);
//
//        Map<String, Object> result = new HashMap<>();
//        if (student != null) {
//            result.put("success", true);
//            result.put("student", student);
//            result.put("message", "登录成功");
//            return ResponseEntity.ok(result);
//        } else {
//            result.put("success", false);
//            result.put("message", "用户名或密码错误");
//            return ResponseEntity.badRequest().body(result);
//        }
//    }
//
//    /**
//     * 修改密码
//     * @param studentId 学生ID
//     * @param oldPassword 旧密码
//     * @param newPassword 新密码
//     * @return 修改结果
//     */
//    @PostMapping("/{studentId}/change-password")
//    public ResponseEntity<Map<String, Object>> changePassword(
//            @PathVariable("studentId") Long studentId,
//            @RequestParam("oldPassword") String oldPassword,
//            @RequestParam("newPassword") String newPassword) {
//        boolean success = studentService.changePassword(studentId, oldPassword, newPassword);
//
//        Map<String, Object> result = new HashMap<>();
//        if (success) {
//            result.put("success", true);
//            result.put("message", "密码修改成功");
//            return ResponseEntity.ok(result);
//        } else {
//            result.put("success", false);
//            result.put("message", "密码修改失败，原密码可能不正确");
//            return ResponseEntity.badRequest().body(result);
//        }
//    }
//
//    /**
//     * 检查用户名是否存在
//     * @param username 用户名
//     * @return 检查结果
//     */
//    @GetMapping("/check/username")
//    public ResponseEntity<Map<String, Object>> checkUsername(@RequestParam("username") String username) {
//        boolean exists = studentService.isUsernameExist(username);
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("exists", exists);
//        return ResponseEntity.ok(result);
//    }
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 检查结果
     */
    @GetMapping("/check/email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam("email") String email) {
        boolean exists = studentService.isEmailExist(email);
        
        Map<String, Object> result = new HashMap<>();
        result.put("exists", exists);
        return ResponseEntity.ok(result);
    }
} 