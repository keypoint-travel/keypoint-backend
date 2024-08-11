package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.dto.CommonTourismDto;
import com.keypoint.keypointtravel.banner.dto.response.CommonBannerSummaryUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.BannerUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.CommonTourismUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.CommonBannerThumbnailDto;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.repository.banner.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindBannerService {

    private final BannerRepository bannerRepository;

    /**
     * 생성한 Banner 목록을 조회하는 함수 (공통 배너 목록 조회(언어 별 모두 조회))
     *
     * @Return 관리자가 확인할 수 있는 배너 목록 useCase
     */
    @Transactional(readOnly = true)
    public List<CommonBannerSummaryUseCase> findBannerList() {
        // fetch join 으로 bannerContents 를 가져옴
        List<Banner> banners = bannerRepository.findBannerList();
        return banners.stream().map(CommonBannerSummaryUseCase::from).toList();
    }

    /**
     * Banner 썸네일 목록을 조회하는 함수 (배너 썸네일 목록 조회)
     *
     * @Return 사용자가 볼 수 있는 배너 썸네일 목록 useCase
     */
    @Transactional(readOnly = true)
    public List<CommonBannerThumbnailDto> findThumbnailList(Long memberId) {

        return bannerRepository.findThumbnailList(memberId);
    }

    /**
     * Banner 상세 정보를 조회하는 함수 (공통 배너 상세 조회(언어 별 구분))
     *
     * @Param language, bannerId, memberId(좋아요 여부 확인을 위한)
     *
     * @Return 공통 배너 상세 정보 useCase
     */
    @Transactional(readOnly = true)
    public CommonTourismUseCase findCommonBanner(BannerUseCase useCase) {
        CommonTourismDto dto = bannerRepository.findBannerById(
            useCase.getLanguageCode(), useCase.getBannerId(), useCase.getMemberId());
//        List<CommentDto> dtoList = bannerRepository.findCommentListById(useCase.getBannerId());
        return new CommonTourismUseCase(dto);
    }
}