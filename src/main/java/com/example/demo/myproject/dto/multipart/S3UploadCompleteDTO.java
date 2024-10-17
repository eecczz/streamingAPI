package com.example.demo.myproject.dto.multipart;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class S3UploadCompleteDTO {
    // initiateUpload에서 얻어온 upload ID
    private String uploadId;

    // initiateUpload에서 얻어온 새 파일명
    private String fileName;

    // 업로드할 파일의 ETag, PartNumber 데이터 목록
    private List<S3UploadPartsDetailDTO> parts;
}