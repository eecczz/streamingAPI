package com.example.demo.myproject.repository;

import com.example.demo.myproject.entity.Memo;
import com.example.demo.myproject.entity.User;
import com.example.demo.myproject.entity.VideoLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoLikeRepository extends JpaRepository<VideoLike, Long> {
    Optional<VideoLike> findByMemoAndUser(Memo memo, User user);
    boolean existsByMemoAndUser(Memo memo, User user);
    long countByMemo(Memo memo);
    List<VideoLike> findByUserOrderByCreatedAtDesc(User user);
}
