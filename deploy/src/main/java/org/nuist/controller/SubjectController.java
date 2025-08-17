package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.nuist.bo.SubjectBO;
import org.nuist.service.SubjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subject")
@SecurityRequirement(name = "BearerAuth")
public class SubjectController {
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Operation(summary = "搜索课程")
    @GetMapping("/search/{keywords}")
    public List<SubjectBO> searchSubjects(@PathVariable String keywords) {
        return subjectService.searchSubjects(keywords);
    }

    @Operation(summary = "获取全部课程")
    @GetMapping("/all")
    public List<SubjectBO> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @Operation(summary = "获取课程")
    @GetMapping("/{id}")
    public SubjectBO getSubjectById(@PathVariable Long id) {
        return subjectService.getSubjectById(id);
    }

    @Operation(summary = "添加课程")
    @PostMapping("/add")
    public Map<String, Object> addSubject(@RequestBody SubjectBO subjectBO) {
        return subjectService.addSubject(subjectBO);
    }

    @Operation(summary = "更新课程")
    @PutMapping("/update")
    public Map<String, Object> updateSubject(@RequestBody SubjectBO subjectBO) {
        return subjectService.updateSubject(subjectBO);
    }

    @Operation(summary = "删除课程")
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteSubject(@PathVariable Long id) {
        return subjectService.deleteSubject(id);
    }





    @Operation(summary = "添加课程和科目的关联关系")
    @PostMapping("/addRelation/{subjectId}/{courseId}")
    public Map<String, Object> addSubjectCourseRelation(@PathVariable Long subjectId, @PathVariable Long courseId) {
        return subjectService.addSubjectCourseRelation(subjectId, courseId);
    }

    @Operation(summary = "获取课程和科目的关联关系")
    @GetMapping("/getCoursesBySubjectId/{subjectId}")
    public List<SubjectBO> getCoursesBySubjectId(@PathVariable Long subjectId) {
        return subjectService.getCoursesBySubjectId(subjectId);
    }

    @Operation(summary = "获取课程和科目的关联关系")
    @GetMapping("/getSubjectByCourseId/{courseId}")
    public SubjectBO getSubjectByCourseId(@PathVariable Long courseId) {
        return subjectService.getSubjectByCourseId(courseId);
    }

    @Operation(summary = "删除课程和科目的关联关系")
    @DeleteMapping("/deleteRelation/{subjectId}/{courseId}")
    public Map<String, Object> deleteSubjectCourseRelation(@PathVariable Long subjectId, @PathVariable Long courseId) {
        return subjectService.deleteSubjectCourseRelation(subjectId, courseId);
    }





}
