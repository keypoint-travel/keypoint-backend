package com.keypoint.keypointtravel.banner.repository.banner;

import com.keypoint.keypointtravel.banner.dto.dto.CommentDto;
import com.keypoint.keypointtravel.banner.dto.dto.CommonTourismDto;
import com.keypoint.keypointtravel.banner.dto.useCase.CommonBannerThumbnailDto;
import com.keypoint.keypointtravel.banner.dto.useCase.EditBannerUseCase;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.BannerContent;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;

import java.util.List;

public interface BannerCustomRepository {

    void updateIsDeletedById(Long bannerId);

    void updateContentIsDeletedById(Long bannerId, LanguageCode languageCode);

    boolean existsBannerContentByBannerId(Long bannerId);

    List<BannerContent> findBannerList();

    List<CommonBannerThumbnailDto> findThumbnailList(Long memberId);

    CommonTourismDto findBannerById(LanguageCode languageCode, Long bannerId, Long memberId);

    List<CommentDto> findCommentListById(Long bannerId);

    boolean isExistBannerContentByLanguageCode(Long bannerId, LanguageCode languageCode);

    void updateBannerContentById(EditBannerUseCase useCase);
}
