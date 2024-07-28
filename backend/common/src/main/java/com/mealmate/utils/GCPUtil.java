package com.mealmate.utils;

import java.io.FileInputStream;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@Slf4j
public class GCPUtil {

    private String bucketName;
    private String projectId;
    private String path;

    public String upload(byte[] bytes, String objectName) {
        log.info("upload file: {}", objectName);
        try {
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(path)))
                    .setProjectId(projectId).build().getService();
            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            Blob blob = storage.create(blobInfo, bytes);

            return "https://storage.googleapis.com/" + bucketName + "/" + objectName;
        } catch (Exception e) {
            log.error("gcp upload file error: {}", e.getMessage());
            return null;
        }
    }
}
