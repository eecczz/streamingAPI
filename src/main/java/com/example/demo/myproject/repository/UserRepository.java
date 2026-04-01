package com.example.demo.myproject.repository;

import com.example.demo.myproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
