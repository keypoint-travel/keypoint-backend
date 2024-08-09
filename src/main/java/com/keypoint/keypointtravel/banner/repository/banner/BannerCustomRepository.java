package com.keypoint.keypointtravel.banner.repository.banner;

import com.keypoint.keypointtravel.banner.dto.dto.CommentDto;
import com.keypoint.keypointtravel.banner.dto.dto.CommonTourismDto;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;

import java.util.List;

public interface BannerCustomRepository {

    void updateIsExposedById(Long bannerId);

    List<Banner> findBannerList();

    CommonTourismDto findBannerById(LanguageCode languageCode, Long bannerId, Long memberId);

    List<CommentDto> findCommentListById(Long bannerId);
}
