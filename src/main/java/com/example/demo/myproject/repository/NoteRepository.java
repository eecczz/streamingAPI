package com.example.demo.myproject.repository;

import com.example.demo.myproject.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByTargetUserIdOrderByCreatedAtDesc(Long targetUserId);
}
