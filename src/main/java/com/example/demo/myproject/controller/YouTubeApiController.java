package com.example.demo.myproject.controller;

import com.example.demo.myproject.dto.api.VideoDTO;
import com.example.demo.myproject.service.YouTubeVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
public class YouTubeApiController {
    private final YouTubeVideoService youTubeVideoService;

    @GetMapping("/videos")
    public List<VideoDTO> searchVideos(@RequestParam(required = false) String query) {
        return youTubeVideoService.searchVideos(query);
    }
}
