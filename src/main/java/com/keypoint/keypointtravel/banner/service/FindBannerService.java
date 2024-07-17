package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.dto.CommentDto;
import com.keypoint.keypointtravel.banner.dto.dto.CommonTourismDto;
import com.keypoint.keypointtravel.banner.dto.useCase.BannerUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.CommonTourismUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.SummaryUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.ThumbnailUseCase;
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
     * 생성한 Banner 목록을 조회하는 함수 (공통 배너 목록 조회)
     *
     * @Return
     */
    @Transactional(readOnly = true)
    public List<SummaryUseCase> findBannerList() {

        List<Banner> banners = bannerRepository.findBannerList();
        return banners.stream().map(SummaryUseCase::from).toList();
    }

    /**
     * Banner 썸네일 목록을 조회하는 함수 (배너 썸네일 목록 조회)
     *
     * @Return
     */
    @Transactional(readOnly = true)
    public List<ThumbnailUseCase> findThumbnailList() {

        //todo: 광고 배너 조회 로직 추가 예정
        List<Banner> banners = bannerRepository.findBannerList();
        return banners.stream().map(ThumbnailUseCase::from).toList();
    }

    /**
     * Banner 상세 정보를 조회하는 함수 (공통 배너 상세 조회)
     *
     * @Return
     */
    @Transactional(readOnly = true)
    public CommonTourismUseCase findCommonBanner(BannerUseCase useCase) {
        CommonTourismDto dto = bannerRepository.findBannerById(useCase.getBannerId(), useCase.getMemberId());
        List<CommentDto> dtoList = bannerRepository.findCommentListById(useCase.getBannerId());
        return new CommonTourismUseCase(dto, dtoList);
    }
}