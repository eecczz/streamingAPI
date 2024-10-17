package com.example.demo.myproject.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemoDTO {

    private Long mno;

    private String memoText;

    private String imageUrl;

    private String videoUrl;
}
