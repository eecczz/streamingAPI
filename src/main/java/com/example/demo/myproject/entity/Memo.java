package com.example.demo.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "tbl_memo")
@ToString(exclude = "uploader")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 1000)
    private String memoText;

    @Column(length = 2000)
    private String description;

    @Column(length = 1000)
    private String imageUrl;

    @Column(length = 1000)
    private String videoUrl;

    @Column(length = 1000)
    private String hlsUrl;

    private Long durationSeconds;

    @Builder.Default
    private Long viewCount = 0L;

    @Builder.Default
    private Long likeCount = 0L;

    @Builder.Default
    private String visibility = "PUBLIC";

    @Builder.Default
    private String uploadStatus = "READY";

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    private User uploader;

    public void setUploader(User uploader) {
        this.uploader = uploader;
        uploader.getMemos().add(this);
    }

    public void increaseViewCount() {
        this.viewCount = this.viewCount == null ? 1L : this.viewCount + 1;
    }

    public void increaseLikeCount() {
        this.likeCount = this.likeCount == null ? 1L : this.likeCount + 1;
    }

    public void decreaseLikeCount() {
        if (this.likeCount == null || this.likeCount == 0) {
            this.likeCount = 0L;
            return;
        }
        this.likeCount -= 1;
    }
}
