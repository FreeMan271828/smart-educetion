package org.nuist.client;

import org.nuist.bo.StudentBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "studentService")
public interface StudentClient {

    /**
     * 获取当前登录学生信息
     * @return 学生信息
     */
    @GetMapping("/api/student/self")
    ResponseEntity<StudentBO> getStudentSelf(UserDetails userDetails);
    
    /**
     * 根据学生ID获取学生信息
     * @param studentId 学生ID
     * @return 学生信息
     */
    @GetMapping("/api/student/{studentId}")
    ResponseEntity<StudentBO> getStudentById(@PathVariable("studentId") Long studentId);
    
    /**
     * 根据用户名获取学生信息
     * @param username 用户名
     * @return 学生信息
     */
    @GetMapping("/api/student/username/{username}")
    ResponseEntity<StudentBO> getStudentByUsername(@PathVariable("username") String username);
    
    /**
     * 根据姓名模糊查询学生信息
     * @param fullName 姓名
     * @return 学生信息列表
     */
    @GetMapping("/api/student/search/name")
    ResponseEntity<List<StudentBO>> getStudentsByFullName(@RequestParam("fullName") String fullName);
    
    /**
     * 根据班级查询学生信息
     * @param grade 年级
     * @param className 班级名称
     * @return 学生信息列表
     */
    @GetMapping("/api/student/class")
    ResponseEntity<List<StudentBO>> getStudentsByClass(
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "className", required = false) String className);
    
    /**
     * 综合搜索学生信息
     * @param keywords 关键词
     * @param grade 年级
     * @param className 班级名称
     * @return 学生信息列表
     */
    @GetMapping("/api/student/search")
    ResponseEntity<List<StudentBO>> searchStudents(
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "className", required = false) String className);
    
    /**
     * 保存或更新学生信息
     * @param student 学生信息
     * @return 保存结果
     */
    @PostMapping("/api/student/save")
    ResponseEntity<Map<String, Object>> saveOrUpdateStudent(@RequestBody StudentBO student);

    /**
     * 学生注册
     * @param dto 注册必要信息
     * @return 认证JWT token
     */
    @PostMapping("/api/student/register")
    ResponseEntity<StudentBO> registerStudent(@RequestBody StudentBO dto);
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 检查结果
     */
    @GetMapping("/api/student/check/email")
    ResponseEntity<Map<String, Object>> checkEmail(@RequestParam("email") String email);
}
