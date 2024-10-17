package com.example.demo.myproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class S3UploadDTO {

    // S3 UploadId
    private String uploadId;
    // 서버에서 생성한 파일 이름
    private String fileName;

    public S3UploadDTO(String uploadId, String fileName) {
        this.uploadId = uploadId;
        this.fileName = fileName;
    }
}