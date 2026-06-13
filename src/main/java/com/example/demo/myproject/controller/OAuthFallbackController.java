package com.example.demo.myproject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuthFallbackController {
    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${GOOGLE_CLIENT_ID:}")
    private String googleClientId;

    @GetMapping("/oauth2/authorization/google")
    public String googleLoginFallback() {
        if (googleClientId != null && !googleClientId.isBlank()) {
            return "forward:/error";
        }
        return "redirect:" + frontendUrl + "?authError=google_oauth_not_configured";
    }
}
