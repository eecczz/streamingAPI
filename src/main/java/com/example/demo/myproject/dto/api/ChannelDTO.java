package com.example.demo.myproject.dto.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChannelDTO {
    private Long id;
    private String userName;
    private String channelName;
    private String avatarUrl;
    private String description;
    private long subscriberCount;
    private boolean subscribed;
}
