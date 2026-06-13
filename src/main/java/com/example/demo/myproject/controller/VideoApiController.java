package com.example.demo.myproject.controller;

import com.example.demo.myproject.dto.api.*;
import com.example.demo.myproject.service.VideoPlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VideoApiController {
    private final VideoPlatformService videoPlatformService;

    @GetMapping("/videos")
    public List<VideoDTO> listVideos(@RequestParam(required = false) Long viewerId) {
        return videoPlatformService.listVideos(viewerId);
    }

    @GetMapping("/videos/{id}")
    public VideoDTO getVideo(@PathVariable Long id, @RequestParam(required = false) Long viewerId) {
        return videoPlatformService.getVideo(id, viewerId);
    }

    @PostMapping("/videos")
    public VideoDTO createVideo(@RequestBody VideoCreateRequest request) {
        return videoPlatformService.createVideo(request);
    }

    @GetMapping("/videos/{id}/comments")
    public List<CommentDTO> listComments(@PathVariable Long id) {
        return videoPlatformService.listComments(id);
    }

    @PostMapping("/videos/{id}/comments")
    public CommentDTO addComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        return videoPlatformService.addComment(id, request);
    }

    @PostMapping("/videos/{id}/like")
    public ToggleResponse toggleLike(@PathVariable Long id, @RequestParam Long userId) {
        return videoPlatformService.toggleLike(id, userId);
    }

    @PostMapping("/channels/{channelOwnerId}/subscribe")
    public ToggleResponse toggleSubscription(@PathVariable Long channelOwnerId, @RequestParam Long subscriberId) {
        return videoPlatformService.toggleSubscription(channelOwnerId, subscriberId);
    }

    @GetMapping("/users/{userId}/notifications")
    public List<NotificationDTO> listNotifications(@PathVariable Long userId) {
        return videoPlatformService.listNotifications(userId);
    }

    @GetMapping("/users/{userId}/liked-videos")
    public List<VideoDTO> listLikedVideos(@PathVariable Long userId) {
        return videoPlatformService.listLikedVideos(userId);
    }

    @GetMapping("/users/{userId}/uploads")
    public List<VideoDTO> listUploadedVideos(@PathVariable Long userId) {
        return videoPlatformService.listUploadedVideos(userId);
    }

    @GetMapping("/users/{userId}/subscriptions")
    public List<ChannelDTO> listSubscribedChannels(@PathVariable Long userId) {
        return videoPlatformService.listSubscribedChannels(userId);
    }
}
