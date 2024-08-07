package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.response.commonBanner.AroundTourism;
import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RecommendationResponse {

    private String contentId;
    private String title;
    private String address1;
    private String address2;
    private String latitude;
    private String longitude;
    private List<AroundTourism> arounds;

    public static RecommendationResponse from(List<Item> items){
        return RecommendationResponse.builder()
            .contentId(items.get(0).getContentid())
            .title(items.get(0).getTitle())
            .address1(items.get(0).getAddr1())
            .address2(items.get(0).getAddr2())
            .latitude(items.get(0).getMapy())
            .longitude(items.get(0).getMapx())
            .arounds(items.stream()
                .skip(1)
                .map(AroundTourism::from).toList())
            .build();
    }
}
