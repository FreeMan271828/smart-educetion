package org.nuist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.nuist.service.MinioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/minio")
@SecurityRequirement(name = "BearerAuth")
public class MinioController {

    private final MinioService minioService;

    @PostMapping("/upload-url")
    @Operation(summary = "生成上传文件URL")
    public ResponseEntity<String> getUploadUrl(@RequestParam String objectName) {
        try {
            return ResponseEntity.ok(minioService.generateUploadUrl(objectName));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("生成上传URL失败：\n" + e.getMessage());
        }
    }

    @PostMapping("/download-url")
    @Operation(summary = "生成下载文件URL")
    public ResponseEntity<String> getDownloadUrl(@RequestParam String objectName) {
        try {
            return ResponseEntity.ok(minioService.generateDownloadUrl(objectName));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("生成下载URL失败：\n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除指定文件")
    public ResponseEntity<String> delete(@RequestParam String objectName) {
        try {
            minioService.deleteObject(objectName);
            return ResponseEntity.ok("删除文件成功");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("删除失败：\n" + e.getMessage());
        }
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除文件")
    public ResponseEntity<String> batchDelete(@RequestBody List<String> objectNames) {
        try {
            minioService.deleteObjects(objectNames);
            return ResponseEntity.ok("批量删除文件成功");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("批量删除失败：\n" + e.getMessage());
        }
    }

    @GetMapping("/list")
    @Operation(summary = "按照文件名前缀（可选）列出文件名")
    public ResponseEntity<List<String>> listFiles(@RequestParam(required = false) String prefix) {
        try {
            return ResponseEntity.ok(
                    minioService.listObjects(prefix == null ? "" : prefix)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(List.of(e.getMessage()));
        }
    }
}
