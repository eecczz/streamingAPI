package com.example.demo.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Table(name = "follow_user")
@ToString(exclude = {"followers", "followings"})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String userName;

    @OneToMany(mappedBy = "followUser", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Follower> followers = new ArrayList<>();

    @OneToMany(mappedBy = "followUser", cascade = CascadeType.ALL)
    @Builder.Default
    private List<User> followings = new ArrayList<>();
}
