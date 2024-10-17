package com.example.demo.myproject.dto.multipart;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class S3UploadInitiateDTO {
    // 업로드할 파일의 originFileName
    private String fileName;
}