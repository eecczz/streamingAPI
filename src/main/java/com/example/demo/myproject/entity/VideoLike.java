package com.example.demo.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(
        name = "video_like",
        uniqueConstraints = @UniqueConstraint(columnNames = {"memo_id", "user_id"})
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
