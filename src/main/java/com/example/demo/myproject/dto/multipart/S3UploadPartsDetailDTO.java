package com.example.demo.myproject.dto.multipart;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class S3UploadPartsDetailDTO {
    private String awsETag;
    private int partNumber;
}