package com.example.demo.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Table(name="followUser")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowUser { // User - Follow 다대다의 중간다리 (다대일-일대다로 쪼개기)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String userName;

    @OneToMany(mappedBy = "followUser", cascade = CascadeType.ALL)
    private List<Follower> followers = new ArrayList<>(); // 나를 구독

    @OneToMany(mappedBy = "followUser", cascade = CascadeType.ALL)
    private List<User> followings = new ArrayList<>(); //나의 구독
}
