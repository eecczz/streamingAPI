package com.example.demo.myproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Value("${GOOGLE_CLIENT_ID:}")
    private String googleClientId;

    public SecurityConfig(OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults()); // HttpSecurity
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(
                isGoogleLoginEnabled() ? SessionCreationPolicy.IF_REQUIRED : SessionCreationPolicy.STATELESS
        ));
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin((formLogin) -> formLogin
                .disable()
        );
        if (isGoogleLoginEnabled()) {
            http.oauth2Login(oauth2 -> oauth2.successHandler(oAuth2LoginSuccessHandler));
        }
        return http.build();
    }

    private boolean isGoogleLoginEnabled() {
        return googleClientId != null && !googleClientId.isBlank();
    }
}
