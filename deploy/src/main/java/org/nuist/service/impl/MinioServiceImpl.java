package org.nuist.service.impl;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nuist.service.MinioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Override
    public String generateUploadUrl(String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(10 * 60)
                        .build()
        );
    }

    @Override
    public String generateDownloadUrl(String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(10 * 60)
                        .build()
        );
    }

    @Override
    public void deleteObject(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    @Override
    public void deleteObjects(List<String> objectNames) throws Exception {
        List<DeleteObject> objects = new ArrayList<>();
        objectNames.forEach(name -> objects.add(new DeleteObject(name)));

        var results = minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                        .bucket(bucketName)
                        .objects(objects)
                        .build()
        );
        for (var result : results) {
            DeleteError error = result.get();
            log.error("Minio删除失败：%s -> %s".formatted(error.objectName(), error.message()));
        }
    }

    @Override
    public List<String> listObjects(String prefix) throws Exception {
        List<String> names = new ArrayList<>();

        var results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(prefix)
                        .recursive(false)
                        .build()
        );
        for (var result : results) {
            names.add(result.get().objectName());
        }

        return names;
    }
}
