package com.vlsu.inventory.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ImageResponse {
    private String imageBase64;
    public ImageResponse(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
