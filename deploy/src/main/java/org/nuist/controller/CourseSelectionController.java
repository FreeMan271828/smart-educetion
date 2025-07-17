package org.nuist.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nuist.bo.CourseBO;
import org.nuist.bo.StudentBO;
import org.nuist.service.CourseSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/course-selection")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "course-selection", description = "学生选课相关API")
public class CourseSelectionController {

    @Autowired
    private CourseSelectionService courseSelectionService;

    private final RedisTemplate<String, String> redisTemplate;

    public CourseSelectionController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Value("${invite.code.length}")
    private int codeLength;

    @Value("${invite.code.expiration.hours}")
    private int expirationHours;


    /**
     * 保存选课记录
     * @param studentId 学生ID
     * @param courseId  课程ID
     * @return
     */
    @PostMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Long> saveCourseSelection(@PathVariable("studentId") Long studentId,
                                                    @PathVariable("courseId") Long courseId) {
        Long courseSelectionId=courseSelectionService.saveCourseSelection(studentId, courseId);
        if( courseSelectionId!= null) {
            return ResponseEntity.ok(courseSelectionId);
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 根据学生ID查询所有已选课程
     * @param studentId 学生ID
     * @return
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<CourseBO>> getCourseSelectionsByStudentId(@PathVariable("studentId") Long studentId){
        List<CourseBO> courseBOs = courseSelectionService.getCourseSelections(studentId);
        if(courseBOs != null){
            return ResponseEntity.ok(courseBOs);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据学生ID和课程ID查询是否选课
     * @param studentId 学生ID
     * @param courseId  课程ID
     * @return
     */
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Boolean> isCourseSelected(@PathVariable("studentId") Long studentId,
                                                   @PathVariable("courseId") Long courseId) {
        boolean isSelected = courseSelectionService.isCourseSelected(studentId, courseId);
        return ResponseEntity.ok(isSelected);
    }

    /**
     * 删除选课记录
     * @param studentId 学生ID
     * @param courseId  课程ID
     * @return
     */
    @DeleteMapping("/batch/student/{studentId}/course/{courseId}")
    public ResponseEntity<Map<String,Object>> deleteCourseSelection(@PathVariable("studentId") Long studentId,
                                                                    @PathVariable("courseId") Long courseId) {
        if(courseSelectionService.deleteCourseSelection(studentId, courseId)) {
            Map<String, Object> result = Map.of("message", "删除成功");
            return ResponseEntity.ok(result);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据课程ID查询所有已选学生
     * @param courseId 课程ID
     * @return
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<StudentBO>> getStudentsByCourseId(@PathVariable("courseId") Long courseId) {
        List<StudentBO> students = courseSelectionService.getStudentsByCourseId(courseId);
        if(students != null){
            return ResponseEntity.ok(students);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 删除对应课程的所有选课数据
     * @param courseId
     * @return
     */
    @DeleteMapping("/batch/course/{courseId}")
    public ResponseEntity<Map<String,Object>> deleteAllCourseSelection(@PathVariable("courseId") Long courseId) {
        if(courseSelectionService.DeleteAllCourseSelection(courseId)) {
            Map<String, Object> result = Map.of("message", "删除成功");
            return ResponseEntity.ok(result);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * 生成邀请码
     *
     * @param courseId 课程ID
     * @return 生成的邀请码和相关信息
     */
    @PostMapping("/generate-invite-code")
    @Operation(summary = "生成邀请码")
    public ResponseEntity<Map<String, Object>> generateInviteCode(@RequestParam String courseId) {
        // 1. 生成6位随机邀请码
        String inviteCode = generateRandomCode(codeLength);

        // 2. 创建Redis键（invite:邀请码）
        String redisKey = "invite:" + inviteCode;

        // 3. 存值（课程ID）并设置过期时间
        redisTemplate.opsForValue().set(redisKey, courseId);
        redisTemplate.expire(redisKey, Duration.ofHours(expirationHours));

        // 4. 响应结果（包含图片中的显示元素）
        Map<String, Object> response = new HashMap<>();
        response.put("invite_code", inviteCode);
        response.put("course_id", courseId);
        response.put("expire_hours", expirationHours);



        return ResponseEntity.ok(response);
    }

    /**
     * 根据邀请码加入课程
     *
     * @param studentId 学生ID
     * @param inviteCode 邀请码
     * @return 加入课程结果
     */
    @PostMapping("/join-by-invite-code")
    @Operation(summary = "根据邀请码加入课程")
    public ResponseEntity<Map<String, Object>> joinCourseByInviteCode(
            @RequestParam String studentId,
            @RequestParam String inviteCode) {

        // 1. 创建Redis键
        String redisKey = "invite:" + inviteCode;

        // 2. 获取课程ID
        String courseId = redisTemplate.opsForValue().get(redisKey);

        if (courseId == null) {
            return ResponseEntity.badRequest().body(createErrorResponse("邀请码不存在或已过期"));
        }

        // 3. 调用加入课程服务
        Long joinResult = courseSelectionService.saveCourseSelection(Long.parseLong(studentId), Long.parseLong(courseId));

        // 4. 根据结果返回响应
        if (joinResult!= null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "成功加入课程");
            response.put("student_id", studentId);
            response.put("course_id", courseId);


            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(createErrorResponse("加入课程失败"));
        }
    }


    // 生成随机邀请码
    private String generateRandomCode(int length) {
        String characters = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }


    // 创建错误响应
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);

        return error;
    }


}
