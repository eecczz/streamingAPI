package com.example.demo.myproject.dto.api;

import lombok.Data;

@Data
public class VideoCreateRequest {
    private Long uploaderId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String videoUrl;
    private String hlsUrl;
    private Long durationSeconds;
}
