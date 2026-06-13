package com.example.demo.myproject.dto.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDTO {
    private Long id;
    private String userName;
    private String email;
    private String channelName;
    private String avatarUrl;
    private String provider;
}
