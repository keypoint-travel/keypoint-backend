package com.keypoint.keypointtravel.banner.dto.response.commonBanner;

import com.keypoint.keypointtravel.banner.dto.useCase.CommonTourismUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.Items;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class CommonBannerResponse {

    private String contentId;
    private String mainTitle;
    private String subTitle;
    private String placeName;
    private String address1;
    private String address2;
    private Double latitude;
    private Double longitude;
    private String thumbnailImage;
    private String cat1;
    private String cat2;
    private String cat3;
    private List<AroundTourism> around;
    // todo: comments, totalLikes, myLike, memberId 추후 고려
//    private List<CommentResponse> comments;
//    private int totalLikes;
//    private boolean myLike;
//
//    private Long memberId;

    public static CommonBannerResponse of(CommonTourismUseCase details, Items data) {
        return CommonBannerResponse.builder()
            .contentId(String.valueOf(details.getCommonTourismDto().getId()))
            .mainTitle(details.getCommonTourismDto().getMainTitle())
            .subTitle(details.getCommonTourismDto().getSubTitle())
            .placeName(details.getCommonTourismDto().getPlaceName())
            .address1(details.getCommonTourismDto().getAddress1())
            .address2(details.getCommonTourismDto().getAddress2())
            .latitude(details.getCommonTourismDto().getLatitude())
            .longitude(details.getCommonTourismDto().getLongitude())
            .thumbnailImage(details.getCommonTourismDto().getThumbnailImage())
            .cat1(details.getCommonTourismDto().getCat1())
            .cat2(details.getCommonTourismDto().getCat2())
            .cat3(details.getCommonTourismDto().getCat3())
            .around(createAroundTourismList(data, details))
//            .comments(createCommentsList(details))
//            .totalLikes(details.getCommonTourismDto().getBannerLikesSize())
//            .myLike(details.getCommonTourismDto().isLiked())
//            .memberId(userDetails == null ? null : userDetails.getId())
            .build();
    }

    private static List<AroundTourism> createAroundTourismList(Items data, CommonTourismUseCase details) {
        return data.getItem().stream()
            .filter(item -> Integer.parseInt(item.getContentid()) != details.getCommonTourismDto().getId())
            .map(AroundTourism::from).toList();
    }

//    private static List<CommentResponse> createCommentsList(CommonTourismUseCase details) {
//        return details.getCommentDtoList().stream().map(CommentResponse::from).toList();
//    }
}
