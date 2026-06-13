package com.example.demo.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Table(name = "user")
@ToString(exclude = {"memos", "comments", "notes", "followUser"})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String userName;

    @Column(length = 255, unique = true)
    private String email;

    @Column(length = 255, unique = true)
    private String googleId;

    @Column(length = 50)
    @Builder.Default
    private String provider = "LOCAL";

    @Column(length = 200)
    private String channelName;

    @Column(length = 500)
    private String avatarUrl;

    @Column(length = 1000)
    private String channelDescription;

    @OneToMany(mappedBy = "uploader", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Memo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private FollowUser followUser;

    @OneToMany(mappedBy = "targetUser", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Note> notes = new ArrayList<>();

    public void setFollowUser(FollowUser followUser) {
        this.followUser = followUser;
        followUser.getFollowings().add(this);
    }
}
