package com.example.demo.myproject.dto.multipart;

import lombok.*;

@NoArgsConstructor
@Data
public class S3UploadResultDTO {

    // S3 URL
    private String url;

    // 파일 명
    private String name;

    // 원본 파일 사이즈 (바이트)
    private long size;

    @Builder
    public S3UploadResultDTO(String url, String name, long size) {
        this.url = url;
        this.name = name;
        this.size = size;
    }
}