package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.banner.dto.request.BannerRequest;
import com.keypoint.keypointtravel.global.enumType.banner.AreaCode;
import com.keypoint.keypointtravel.global.enumType.banner.BannerCode;
import com.keypoint.keypointtravel.global.enumType.banner.ContentType;
import com.keypoint.keypointtravel.global.enumType.banner.LargeCategory;
import com.keypoint.keypointtravel.global.enumType.banner.MiddleCategory;
import com.keypoint.keypointtravel.global.enumType.banner.SmallCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SaveUseCase {

    private Long contentId;
    private String title;
    private AreaCode areaCode;
    private LargeCategory cat1;
    private MiddleCategory cat2;
    private SmallCategory cat3;
    private ContentType contentType;
    private String thumbnailTitle;
    private String thumbnailImage;
    private String address1;
    private String address2;
    private Double latitude;
    private Double longitude;

    public static SaveUseCase from(BannerRequest request){
        return SaveUseCase.builder()
            .contentId(request.getContentId())
            .title(request.getTitle())
            .areaCode(BannerCode.getConstant(AreaCode.class, request.getRegion()))
            .cat1(BannerCode.getConstant(LargeCategory.class, request.getCat1()))
            .cat2(BannerCode.getConstant(MiddleCategory.class, request.getCat2()))
            .cat3(BannerCode.getConstant(SmallCategory.class, request.getCat3()))
            .contentType(BannerCode.getConstant(ContentType.class, request.getTourType()))
            .thumbnailTitle(request.getThumbnailTitle())
            .thumbnailImage(request.getThumbnailImage())
            .address1(request.getAddress1())
            .address2(request.getAddress2())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .build();
    }
}