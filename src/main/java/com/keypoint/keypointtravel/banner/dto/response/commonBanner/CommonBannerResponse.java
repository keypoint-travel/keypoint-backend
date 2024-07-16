package com.keypoint.keypointtravel.banner.dto.response.commonBanner;

import com.keypoint.keypointtravel.banner.dto.useCase.CommonTourismUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.Items;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class CommonBannerResponse {

    private Long bannerId;
    private String thumbnailTitle;
    private String title;
    private String address1;
    private String address2;
    private Double latitude;
    private Double longitude;

    private List<AroundTourism> arounds;
    private List<Comments> comments;
    private int totalLikes;
    private boolean myLike;

    private Long memberId;

    public static CommonBannerResponse of(CommonTourismUseCase details, Items data, CustomUserDetails userDetails) {
        return CommonBannerResponse.builder()
            .bannerId(details.getCommonTourismDto().getId())
            .thumbnailTitle(details.getCommonTourismDto().getThumbnailTitle())
            .title(details.getCommonTourismDto().getTitle())
            .address1(details.getCommonTourismDto().getAddress1())
            .address2(details.getCommonTourismDto().getAddress2())
            .latitude(details.getCommonTourismDto().getLatitude())
            .longitude(details.getCommonTourismDto().getLongitude())
            .arounds(data.getItem().stream().map(AroundTourism::from).toList())
            .comments(details.getCommentDtoList().stream().map(Comments::from).toList())
            .totalLikes(details.getCommonTourismDto().getBannerLikesSize())
            .myLike(details.getCommonTourismDto().isLiked())
            .memberId(userDetails == null ? null : userDetails.getId())
            .build();
    }
}
