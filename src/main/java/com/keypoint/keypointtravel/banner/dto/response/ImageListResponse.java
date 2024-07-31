package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.useCase.imageListUseCase.Items;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ImageListResponse {

    private String contentId;
    private List<ImageUrlResponse> images;

    public static ImageListResponse of(String contentId, Items items) {
        return ImageListResponse.builder()
            .contentId(contentId)
            .images(items.getItem().stream()
                .map(item -> new ImageUrlResponse(item.getOriginimgurl()))
                .toList())
            .build();
    }
}
