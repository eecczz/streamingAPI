package com.example.demo.myproject.dto.api;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentDTO {
    private Long id;
    private String text;
    private Long parentCommentId;
    private LocalDateTime createdAt;
    private Long likeCount;
    private ChannelDTO author;
    private List<CommentDTO> replies;
}
