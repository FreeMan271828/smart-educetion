package org.nuist.service;

import java.util.List;

public interface MinioService {

    public String generateUploadUrl(String objectName) throws Exception;

    public String generateDownloadUrl(String objectName) throws Exception;

    public void deleteObject(String objectName) throws Exception;

    public void deleteObjects(List<String> objectNames) throws Exception;

    public List<String> listObjects(String prefix) throws Exception;
}
