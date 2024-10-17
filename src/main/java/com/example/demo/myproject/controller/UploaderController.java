package com.example.demo.myproject.controller;

import com.example.demo.myproject.config.S3Config;
import com.example.demo.myproject.dto.S3UploadDTO;
import com.example.demo.myproject.dto.multipart.*;
import com.example.demo.myproject.service.MemoService;
import com.example.demo.myproject.service.S3MultipartUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UploaderController
{
    private final S3MultipartUploader s3MultipartUploader;

    @Value("${cloud.aws.s3.bucket}")
    private String videoBucket;

    @PostMapping("/initiate-upload")
    public S3UploadDTO initiateUpload(@RequestBody S3UploadInitiateDTO s3UploadInitiateDTO) {
        return s3MultipartUploader.initiateUpload(s3UploadInitiateDTO.getFileName(), videoBucket, S3Config.videoFolder);
    }

    @PostMapping("/upload-signed-url")
    public S3PresignedUrlDTO getUploadSignedUrl(@RequestBody S3UploadSignedUrlDTO s3UploadSignedUrlDTO) {
        return s3MultipartUploader.getUploadSignedUrl(s3UploadSignedUrlDTO, videoBucket,S3Config.videoFolder);
    }

    @PostMapping("/complete-upload")
    public S3UploadResultDTO completeUpload(@RequestBody S3UploadCompleteDTO s3UploadCompleteDTO) {
        return s3MultipartUploader.completeUpload(s3UploadCompleteDTO, videoBucket, S3Config.videoFolder);
    }

    @PostMapping("/abort-upload")
    public Void abortUpload(@RequestBody S3UploadAbortDTO s3UploadAbortDTO) {
        s3MultipartUploader.abortUpload(s3UploadAbortDTO, videoBucket, S3Config.videoFolder);
        return null;
    }
}
