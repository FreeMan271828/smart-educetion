package org.nuist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.nuist.dto.FileInfoDTO;
import org.nuist.service.CourseFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/course-file")
@Tag(name = "course-file", description = "课程文件相关API")
public class CourseFileController {
    @Autowired
    private CourseFileService courseFileService;

    /**
     * 上传课程文件
     * @param courseId 课程ID
     **/
    @PostMapping("/upload/courseId/{courseId}")
    public ResponseEntity<Map<String, Object>> uploadCourseFile(@PathVariable("courseId") Long courseId,
                                                                @RequestBody MultipartFile file
    ) {
        return courseFileService.uploadCourseFile(courseId, file);
    }


    /**
     * 获取课程所有文件ID
     * @param courseId 课程ID
     **/
    @GetMapping("/courseId/{courseId}")
    public ResponseEntity<List<FileInfoDTO>> getCourseFiles(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(courseFileService.getFilesByCourseId(courseId));
    }

    /**
     * 删除文件
     * @param fileId 文件ID
     **/
    @DeleteMapping("/fileId/{fileId}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable("fileId") Long fileId) {
        return courseFileService.deleteFileById(fileId);
    }

    /**
     * 下载文件
     * @param fileId 文件ID
     **/
    @GetMapping("/download/fileId/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Long fileId) {
        return courseFileService.downloadFile(fileId);
    }
}
