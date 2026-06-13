package com.example.demo.myproject.controller;

import com.example.demo.myproject.dto.multipart.S3UploadResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class FileUploadApiController {
    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");

    @Value("${spring.file-dir}")
    private String fileDir;

    @PostMapping("/thumbnail")
    public S3UploadResultDTO uploadThumbnail(@RequestPart("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Thumbnail file is empty");
        }
        String extension = extension(file.getOriginalFilename());
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Only jpg, jpeg, png, and webp thumbnails are allowed");
        }

        Path uploadDir = Path.of(System.getProperty("user.dir") + fileDir, "uploads");
        Files.createDirectories(uploadDir);
        String storedName = UUID.randomUUID() + "." + extension;
        Path target = uploadDir.resolve(storedName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/uploads/")
                .path(storedName)
                .toUriString();
        return S3UploadResultDTO.builder()
                .name(storedName)
                .url(url)
                .size(file.getSize())
                .build();
    }

    private String extension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("File extension is required");
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}
