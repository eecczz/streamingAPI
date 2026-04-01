package com.example.demo.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Table(name="user")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String userName;

    @OneToMany(mappedBy = "uploader", cascade = CascadeType.ALL)
    private List<Memo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private FollowUser followUser;

    @OneToMany(mappedBy = "targetUser", cascade = CascadeType.ALL)
    private List<Note> notes = new ArrayList<>();

    public void setFollowUser(FollowUser followUser) {
        this.followUser = followUser;
        followUser.getFollowings().add(this);
    }
}
