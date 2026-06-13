package com.example.demo.myproject.service;

import com.example.demo.myproject.dto.api.ChannelDTO;
import com.example.demo.myproject.dto.api.VideoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YouTubeVideoService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${youtube.api-key:}")
    private String apiKey;

    public List<VideoDTO> searchVideos(String query) {
        if (apiKey == null || apiKey.isBlank()) {
            return List.of();
        }

        String url = UriComponentsBuilder.fromUriString("https://www.googleapis.com/youtube/v3/search")
                .queryParam("part", "snippet")
                .queryParam("type", "video")
                .queryParam("maxResults", 12)
                .queryParam("q", query == null || query.isBlank() ? "programming" : query)
                .queryParam("key", apiKey)
                .build()
                .toUriString();

        Map<?, ?> response = restTemplate.getForObject(url, Map.class);
        Object itemValue = response == null ? null : response.get("items");
        List<?> items = itemValue instanceof List<?> list ? list : List.of();
        List<VideoDTO> videos = new ArrayList<>();
        long syntheticId = -1L;

        for (Object itemObject : items) {
            Map<?, ?> item = (Map<?, ?>) itemObject;
            Map<?, ?> id = (Map<?, ?>) item.get("id");
            Map<?, ?> snippet = (Map<?, ?>) item.get("snippet");
            if (id == null || snippet == null) {
                continue;
            }
            String videoId = value(id.get("videoId"));
            Map<?, ?> thumbnails = (Map<?, ?>) snippet.get("thumbnails");
            String thumbnailUrl = thumbnail(thumbnails);
            String channelTitle = value(snippet.get("channelTitle"));

            videos.add(VideoDTO.builder()
                    .id(syntheticId--)
                    .title(value(snippet.get("title")))
                    .description(value(snippet.get("description")))
                    .thumbnailUrl(thumbnailUrl)
                    .videoUrl("")
                    .externalUrl("https://www.youtube.com/embed/" + videoId)
                    .external(true)
                    .viewCount(0L)
                    .likeCount(0L)
                    .commentCount(0L)
                    .liked(false)
                    .durationSeconds(0L)
                    .uploadStatus("EXTERNAL")
                    .channel(ChannelDTO.builder()
                            .id(0L)
                            .channelName(channelTitle)
                            .userName(channelTitle)
                            .avatarUrl("https://api.dicebear.com/8.x/initials/svg?seed=" + channelTitle)
                            .subscriberCount(0)
                            .subscribed(false)
                            .build())
                    .build());
        }
        return videos;
    }

    private String thumbnail(Map<?, ?> thumbnails) {
        if (thumbnails == null) {
            return "";
        }
        Object highValue = thumbnails.get("high") == null ? thumbnails.get("default") : thumbnails.get("high");
        Map<?, ?> high = highValue instanceof Map<?, ?> map ? map : null;
        return high == null ? "" : value(high.get("url"));
    }

    private String value(Object value) {
        return value == null ? "" : value.toString();
    }
}
