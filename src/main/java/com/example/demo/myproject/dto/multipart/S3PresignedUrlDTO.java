package com.example.demo.myproject.dto.multipart;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class S3PresignedUrlDTO {
    private String preSignedUrl;
}