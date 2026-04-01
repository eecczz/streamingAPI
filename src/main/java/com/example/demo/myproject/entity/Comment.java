package com.example.demo.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name="comment")
@ToString
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

    public void setCommenter(User commenter) {
        this.commenter = commenter;
        commenter.getComments().add(this);
    }
}
