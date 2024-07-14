package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.BannerSummaryUseCase;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindBannerService {

    private final BannerRepository bannerRepository;

    public List<BannerSummaryUseCase> findBannerList() {

        List<Banner> banners = bannerRepository.findBannerList();
        return banners.stream().map(BannerSummaryUseCase::from).toList();
    }
}
