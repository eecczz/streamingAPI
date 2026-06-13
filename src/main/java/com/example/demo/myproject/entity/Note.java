package com.example.demo.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "note")
@ToString(exclude = "targetUser")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String noteText;

    @ManyToOne(fetch = FetchType.LAZY)
    private User targetUser;

    private String notingUserName;

    @Column(length = 50)
    private String notificationType;

    private Long sourceMemoId;
    private Long sourceCommentId;

    @Column(name = "notification_read")
    @Builder.Default
    private boolean readFlag = false;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
        targetUser.getNotes().add(this);
    }
}
