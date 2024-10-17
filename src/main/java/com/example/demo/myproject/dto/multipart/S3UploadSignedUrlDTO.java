package com.example.demo.myproject.dto.multipart;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class S3UploadSignedUrlDTO {
    // initiateUpload에서 얻어온 upload ID
    private String uploadId;

    // initiateUpload에서 얻어온 새 파일명
    private String fileName;

    // 업로드할 파일 조각 Number ( 1부터 시작 )
    private int partNumber;
}