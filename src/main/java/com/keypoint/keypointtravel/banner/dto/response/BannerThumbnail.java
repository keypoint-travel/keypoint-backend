package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.global.constants.TourismApiConstants;
import com.keypoint.keypointtravel.global.enumType.banner.BannerType;
import lombok.Getter;

@Getter
public class BannerThumbnail {

    private String contentId;
    private String thumbnailImage;
    private String mainTitle;
    private String subTitle;
    private BannerType bannerType;

    public BannerThumbnail(String contentId, String thumbnailImage, String mainTitle, String subTitle, BannerType bannerType) {
        String imagePath = thumbnailImage;
        if(imagePath == null || imagePath.isEmpty() || imagePath.isBlank()) {
            imagePath = TourismApiConstants.DEFAULT_IMAGE;
        }
        this.contentId = contentId;
        this.thumbnailImage = imagePath;
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.bannerType = bannerType;
    }
}
