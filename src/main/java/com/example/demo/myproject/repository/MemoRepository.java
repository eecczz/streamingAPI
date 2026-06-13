package com.example.demo.myproject.repository;

import com.example.demo.myproject.entity.Memo;
import com.example.demo.myproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findAllByOrderByCreatedAtDesc();
    List<Memo> findByUploaderOrderByCreatedAtDesc(User uploader);
}
