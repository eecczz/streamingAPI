package com.example.demo.myproject.service;

import com.example.demo.myproject.entity.User;
import com.example.demo.myproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleOAuthMemberService {
    private final UserRepository userRepository;

    public User upsertGoogleMember(Map<String, Object> attributes) {
        String googleId = stringValue(attributes.get("sub"));
        String email = stringValue(attributes.get("email"));
        String name = stringValue(attributes.get("name"));
        String picture = stringValue(attributes.get("picture"));
        String channelName = name == null || name.isBlank() ? email : name;
        String userName = email == null ? "google-" + googleId : email.substring(0, email.indexOf("@"));

        User user = userRepository.findByGoogleId(googleId)
                .or(() -> userRepository.findByEmail(email))
                .orElseGet(User::new);
        user.setGoogleId(googleId);
        user.setEmail(email);
        user.setProvider("GOOGLE");
        user.setUserName(userName);
        user.setChannelName(channelName);
        user.setAvatarUrl(picture);
        user.setChannelDescription(channelName + "'s channel");
        return userRepository.save(user);
    }

    private String stringValue(Object value) {
        return value == null ? null : value.toString();
    }
}
