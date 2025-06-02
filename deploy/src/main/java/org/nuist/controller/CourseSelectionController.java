package org.nuist.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nuist.bo.CourseBO;
import org.nuist.bo.StudentBO;
import org.nuist.service.CourseSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/course-selection")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "course-selection", description = "学生选课相关API")
public class CourseSelectionController {

    @Autowired
    private CourseSelectionService courseSelectionService;


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
}
