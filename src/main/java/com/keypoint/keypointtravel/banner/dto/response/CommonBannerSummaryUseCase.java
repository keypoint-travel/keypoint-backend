package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.entity.BannerContent;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonBannerSummaryUseCase {

    private Long contentId;
    private String region;
    private String tourType;
    private String cat1;
    private String cat2;
    private String cat3;
    private String thumbnailImage;
    private String mainTitle;
    private String subTitle;
    private String placeName;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public static CommonBannerSummaryUseCase from(BannerContent bannerContent) {
        return new CommonBannerSummaryUseCase(
            bannerContent.getBanner().getId(),
            bannerContent.getBanner().getAreaCode().getEngDescription(),
            bannerContent.getContentType(),
            bannerContent.getCat1(),
            bannerContent.getCat2(),
            bannerContent.getCat3(),
            bannerContent.getThumbnailImage(),
            bannerContent.getMainTitle(),
            bannerContent.getSubTitle(),
            bannerContent.getPlaceName(),
            bannerContent.getModifyAt(),
            bannerContent.getCreateAt()
        );
    }
}
