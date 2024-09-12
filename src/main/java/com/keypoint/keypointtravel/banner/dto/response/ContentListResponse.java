package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.Body;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ContentListResponse {

    //content
    private List<BannerDetails> content;

    //page 정보
    private int total;

    public static ContentListResponse from(Body data, LanguageCode languageCode) {
        return ContentListResponse.builder()
            .content(data.getItems().getItem().stream()
                .map(item -> BannerDetails.from(item, languageCode)).toList())
            .total(data.getTotalCount())
            .build();
    }
}