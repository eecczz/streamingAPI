package com.example.demo.myproject.dto.api;

import lombok.Data;

@Data
public class CommentRequest {
    private Long userId;
    private String text;
    private Long parentCommentId;
}
