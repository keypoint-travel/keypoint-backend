package com.keypoint.keypointtravel.banner.dto.response.commonBanner;

import com.keypoint.keypointtravel.banner.dto.dto.ManageCommonTourismDto;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ManageCommonInfo {

    private LanguageCode languageCode;
    private String mainTitle;
    private String subTitle;
    private String placeName;
    private String address1;
    private String address2;
    private String thumbnailImage;
    private String cat1;
    private String cat2;
    private String cat3;
    // todo: comments, totalLikes 추후 고려

    public static ManageCommonInfo from(ManageCommonTourismDto dto){
        return new ManageCommonInfo(
            dto.getLanguageCode(),
            dto.getMainTitle(),
            dto.getSubTitle(),
            dto.getPlaceName(),
            dto.getAddress1(),
            dto.getAddress2(),
            dto.getThumbnailImage(),
            dto.getCat1(),
            dto.getCat2(),
            dto.getCat3()
        );
    }
}
