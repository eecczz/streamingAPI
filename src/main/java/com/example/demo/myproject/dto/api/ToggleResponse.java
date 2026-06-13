package com.example.demo.myproject.dto.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ToggleResponse {
    private boolean active;
    private long count;
}
