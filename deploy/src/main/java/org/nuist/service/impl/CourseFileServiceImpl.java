package org.nuist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nuist.dto.FileInfoDTO;
import org.nuist.mapper.CourseFileMapper;
import org.nuist.po.CourseFilePO;
import org.nuist.service.CourseFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseFileServiceImpl extends ServiceImpl<CourseFileMapper,CourseFilePO> implements CourseFileService {
    @Value("${file.upload.dir}")
    private String uploadDir;

   /* @Value("${file.base.url}")
    private String fileBaseUrl;*/

    @Autowired
    private CourseFileMapper courseFileMapper;
    @Override
    public ResponseEntity<Map<String, Object>> uploadCourseFile(Long courseId, MultipartFile file) {
        try {
            // 1. 验证文件不为空
            if (file.isEmpty()) {
                Map<String, Object> error = Map.of(
                        "status", "error",
                        "message", "上传的文件不能为空"
                );
                return ResponseEntity.badRequest().body(error);
            }

            // 2. 创建课程专属目录
            Path courseDir = Paths.get(uploadDir, String.valueOf(courseId));
            if (!Files.exists(courseDir)) {
                Files.createDirectories(courseDir);
            }

            // 3. 生成唯一存储文件名
            String originalName = file.getOriginalFilename();
            String fileExtension = originalName.substring(originalName.lastIndexOf("."));
            String storedName = UUID.randomUUID().toString() + fileExtension;

            // 4. 保存文件到服务器
            Path filePath = courseDir.resolve(storedName);
            Files.copy(file.getInputStream(), filePath);

            // 5. 构建文件访问URL
           // String fileUrl = fileBaseUrl + courseId + "/" + storedName;

            // 6. 构建数据库记录
            CourseFilePO courseFile = new CourseFilePO();
            courseFile.setCourseId(courseId);
            courseFile.setOriginalName(originalName);
            courseFile.setStoredName(storedName);
            courseFile.setFileUrl(filePath.toString());

            // 7. 使用 MyBatis 自带方法插入记录
            int result = courseFileMapper.insert(courseFile);

            if (result > 0) {
                // 8. 返回成功响应
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "文件上传成功");
                response.put("fileId", courseFile.getFileId());
                response.put("fileUrl", filePath);
                response.put("storedName", storedName);
                return ResponseEntity.ok(response);
            } else {
                // 插入失败时删除已保存文件
                Files.deleteIfExists(filePath);
                return ResponseEntity.internalServerError().body(Map.of(
                        "status", "error",
                        "message", "数据库记录创建失败"
                ));
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "文件处理失败: " + e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "系统错误: " + e.getMessage()
            ));
        }
    }



    @Override
    public List<FileInfoDTO> getFilesByCourseId(Long courseId) {

        LambdaQueryWrapper<CourseFilePO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(CourseFilePO::getCourseId, courseId)
                .select(CourseFilePO::getFileId, CourseFilePO::getOriginalName);

        return baseMapper.selectList(queryWrapper)
                .stream()
                .map(file -> new FileInfoDTO(file.getFileId(), file.getOriginalName()))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Map<String, Object>> deleteFileById(Long fileId) {
        try {
            // 1. 查询文件记录
            CourseFilePO fileRecord = baseMapper.selectById(fileId);

            if (fileRecord == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "status", "error",
                                "message", "文件记录不存在"
                        ));
            }

            // 2. 构造文件路径
            Path filePath = Paths.get(uploadDir,
                    String.valueOf(fileRecord.getCourseId()),
                    fileRecord.getStoredName());

            // 3. 删除物理文件
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            // 4. 删除数据库记录（使用 MyBatis-Plus deleteById）
            int result = baseMapper.deleteById(fileId);

            if (result > 0) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "文件删除成功"
                ));
            } else {
                return ResponseEntity.internalServerError().body(Map.of(
                        "status", "error",
                        "message", "数据库记录删除失败"
                ));
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "物理文件删除失败: " + e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "系统错误: " + e.getMessage()
            ));
        }
    }

    @Override
    public ResponseEntity<Resource> downloadFile(Long fileId) {
        try {
            // 1. 查询文件记录
            CourseFilePO fileRecord = baseMapper.selectById(fileId);

            if (fileRecord == null) {
                return ResponseEntity.notFound().build();
            }

            // 2. 构造文件路径
            Path filePath = Paths.get(uploadDir,
                    String.valueOf(fileRecord.getCourseId()),
                    fileRecord.getStoredName());

            // 3. 获取文件资源
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // 4. 设置响应头
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + encodeFilename(fileRecord.getOriginalName()) + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 处理中文文件名编码问题
     */
    private String encodeFilename(String filename) {
        try {
            return URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            return filename;
        }
    }
}
