package org.nuist.service;

import org.nuist.dto.FileInfoDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CourseFileService {
    ResponseEntity<Map<String, Object>> uploadCourseFile(Long courseId, MultipartFile file);

    // 1. 根据 courseId 获取所有文件信息
    List<FileInfoDTO> getFilesByCourseId(Long courseId);

    // 2. 根据 fileId 删除文件
    ResponseEntity<Map<String, Object>> deleteFileById(Long fileId);

    // 3. 根据 fileId 下载文件
    ResponseEntity<Resource> downloadFile(Long fileId);

}
