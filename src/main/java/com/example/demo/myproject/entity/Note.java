package com.example.demo.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name="note")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String noteText; // 타킷유저에게 어떤 행위 (댓/답글, 새 게시글)를 알릴 것인지, 즉 알림 내용은 service 에서 결정.

    @ManyToOne(fetch = FetchType.LAZY)
    private User targetUser;

    private String notingUserName; // 노팅유저이름도 service에서 결정.

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
        targetUser.getNotes().add(this);
    }
}
