package com.example.demo.myproject.controller;

import com.example.demo.myproject.dto.api.MemberDTO;
import com.example.demo.myproject.entity.User;
import com.example.demo.myproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {
    private final UserRepository userRepository;

    @Value("${GOOGLE_CLIENT_ID:}")
    private String googleClientId;

    @GetMapping("/google/enabled")
    public boolean googleEnabled() {
        return googleClientId != null && !googleClientId.isBlank();
    }

    @GetMapping("/members/{id}")
    public MemberDTO getMember(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));
        return MemberDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .channelName(user.getChannelName())
                .avatarUrl(user.getAvatarUrl())
                .provider(user.getProvider())
                .build();
    }
}
