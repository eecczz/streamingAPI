package com.example.demo.myproject.repository;

import com.example.demo.myproject.entity.Follower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
}
