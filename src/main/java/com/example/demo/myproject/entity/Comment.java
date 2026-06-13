package com.example.demo.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "comment")
@ToString(exclude = "commenter")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String replyText;

    @ManyToOne(fetch = FetchType.LAZY)
    private User commenter;

    private Long memoId;
    private Long parentCommentId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private Long likeCount = 0L;

    public void setCommenter(User commenter) {
        this.commenter = commenter;
        commenter.getComments().add(this);
    }
}
