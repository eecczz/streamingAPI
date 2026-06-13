package com.example.demo.myproject.dto.api;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDTO {
    private Long id;
    private String text;
    private String type;
    private String actorName;
    private Long sourceMemoId;
    private Long sourceCommentId;
    private boolean read;
    private LocalDateTime createdAt;
}
