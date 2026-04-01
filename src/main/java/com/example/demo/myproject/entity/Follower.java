package com.example.demo.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name="follower")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY)
    private FollowUser followUser;

    public void setFollowUser(FollowUser followUser) {
        this.followUser = followUser;
        followUser.getFollowers().add(this);
    }
}
