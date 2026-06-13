package com.example.demo.myproject.repository;

import com.example.demo.myproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMemoIdAndParentCommentIdIsNullOrderByCreatedAtAsc(Long memoId);
    List<Comment> findByParentCommentIdOrderByCreatedAtAsc(Long parentCommentId);
    long countByMemoId(Long memoId);
}
