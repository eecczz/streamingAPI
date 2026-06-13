package com.example.demo.myproject.dto.api;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VideoDTO {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String videoUrl;
    private String hlsUrl;
    private String externalUrl;
    private Long durationSeconds;
    private Long viewCount;
    private Long likeCount;
    private long commentCount;
    private boolean liked;
    private boolean external;
    private String uploadStatus;
    private LocalDateTime createdAt;
    private ChannelDTO channel;
}
