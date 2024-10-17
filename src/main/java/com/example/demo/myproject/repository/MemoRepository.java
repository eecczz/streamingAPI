package com.example.demo.myproject.repository;

import com.example.demo.myproject.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {
}
