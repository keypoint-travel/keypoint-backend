package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.SummaryUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.ThumbnailUseCase;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindBannerService {

    private final BannerRepository bannerRepository;

    @Transactional(readOnly = true)
    public List<SummaryUseCase> findBannerList() {

        List<Banner> banners = bannerRepository.findBannerList();
        return banners.stream().map(SummaryUseCase::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ThumbnailUseCase> findThumbnailList() {

        //todo: 광고 배너 조회 로직 추가 예정
        List<Banner> banners = bannerRepository.findBannerList();
        return banners.stream().map(ThumbnailUseCase::from).toList();
    }
}
