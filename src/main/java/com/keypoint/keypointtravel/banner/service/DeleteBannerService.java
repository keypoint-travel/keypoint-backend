package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.DeleteUseCase;
import com.keypoint.keypointtravel.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteBannerService {

    private final BannerRepository bannerRepository;

    @Transactional
    public void deleteBanner(DeleteUseCase deleteUseCase) {
        bannerRepository.deleteBannerById(deleteUseCase.bannerId());
    }
}