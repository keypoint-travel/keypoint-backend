package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementBannerDto;
import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementDetailDto;
import com.keypoint.keypointtravel.banner.dto.response.AdvertisementBannerUseCase;
import com.keypoint.keypointtravel.banner.dto.response.ImageUrlResponse;
import com.keypoint.keypointtravel.banner.dto.useCase.*;
import com.keypoint.keypointtravel.banner.entity.AdvertisementBanner;
import com.keypoint.keypointtravel.banner.entity.AdvertisementBannerContent;
import com.keypoint.keypointtravel.banner.repository.banner.AdvertisementBannerContentRepository;
import com.keypoint.keypointtravel.banner.repository.banner.AdvertisementBannerRepository;
import com.keypoint.keypointtravel.external.aws.service.S3Service;
import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementBannerService {

    private final S3Service s3Service;

    private final UploadFileService uploadFileService;

    private final AdvertisementBannerRepository advertisementBannerRepository;

    private final AdvertisementBannerContentRepository advertisementBannerContentRepository;

    /**
     * 이미지 파일을 url로 변환하는 함수(광고 배너 생성 시 마크다운 언어로 이미지를 넣을 수 있도록 하기 위함)
     *
     * @Param 이미지 파일 useCase
     * @Return 이미지 파일의 url
     */
    @Transactional
    public ImageUrlResponse uploadImage(ImageUseCase useCase) {
        try {
            String fileName = s3Service.uploadFileInS3(useCase.getImage(),
                DirectoryConstants.ADVERTISEMENT_BANNER_CONTENT_DIRECTORY);
            return new ImageUrlResponse(fileName);
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }

    /**
     * 광고 배너를 생성하는 함수
     *
     * @Param 썸네일 이미지 파일, 상세 이미지 파일, 제목, 내용, 언어 useCase
     */
    @Transactional
    public void saveAdvertisementBanner(AdvertisementUseCase useCase) {
        try {
            // 1. 썸네일 이미지 저장
            Long thumbnailImageId = uploadFileService.saveUploadFile(
                useCase.getThumbnailImage(),
                DirectoryConstants.ADVERTISEMENT_BANNER_THUMBNAIL_DIRECTORY
            );
            // 2. 상세 이미지 저장
            Long detailImageId = uploadFileService.saveUploadFile(
                useCase.getDetailImage(),
                DirectoryConstants.ADVERTISEMENT_BANNER_DETAIL_DIRECTORY
            );
            // 3. 광고 배너 저장
            saveBanner(useCase, thumbnailImageId, detailImageId);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    private void saveBanner(AdvertisementUseCase useCase, Long thumbnailImageId, Long detailImageId) {
        // 배너 생성
        AdvertisementBanner banner = new AdvertisementBanner(thumbnailImageId, detailImageId);
        // 배너 내용 생성
        AdvertisementBannerContent bannerContent = AdvertisementBannerContent.builder()
            .languageCode(useCase.getLanguage())
            .advertisementBanner(banner)
            .mainTitle(useCase.getMainTitle())
            .subTitle(useCase.getSubTitle())
            .content(useCase.getContent())
            .build();
        // 배너 저장
        advertisementBannerRepository.save(banner);
        advertisementBannerContentRepository.save(bannerContent);
    }

    /**
     * 이미 생성된 배너에 다른 언어로 생성하는 함수 (광고 배너 생성)
     *
     * @Param 제목, 내용, 언어, 배너 id useCase
     */
    @Transactional
    public void saveBannerByOtherLanguage(PlusAdvertisementUseCase useCase) {
        //이미 bannerId에 해당하는 배너에 저장할 언어로 배너 내용이 있는지 확인
        if (advertisementBannerRepository.isExistBannerContentByLanguageCode(useCase.getBannerId(), useCase.getLanguage())) {
            throw new GeneralException(BannerErrorCode.EXISTS_BANNER_CONTENT);
        }
        try {
            // 배너 조회
            AdvertisementBanner banner = advertisementBannerRepository.getReferenceById(useCase.getBannerId());
            // 배너 내용 생성
            AdvertisementBannerContent bannerContent = AdvertisementBannerContent.builder()
                .languageCode(useCase.getLanguage())
                .advertisementBanner(banner)
                .mainTitle(useCase.getMainTitle())
                .subTitle(useCase.getSubTitle())
                .content(useCase.getContent())
                .build();
            // 배너 내용 저장
            advertisementBannerContentRepository.save(bannerContent);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    /**
     * 광고 배너 목록 조회 함수
     *
     * @Return
     */
    @Transactional(readOnly = true)
    public List<AdvertisementBannerUseCase> findAdvertisementBanners() {
        List<AdvertisementBannerDto> dtoList = advertisementBannerRepository.findAdvertisementBanners();
        // 배너 목록을 contentId를 key로 하는 useCase 배열로 저장
        List<AdvertisementBannerUseCase> useCases = conversionToUseCase(dtoList);
        Collections.reverse(useCases);
        // 배너 내용을 최신순으로 정렬
        useCases.forEach(AdvertisementBannerUseCase::sortContents);
        return useCases;
    }

    private List<AdvertisementBannerUseCase> conversionToUseCase(List<AdvertisementBannerDto> dtoList) {
        HashMap<String, AdvertisementBannerUseCase> useCaseMap = new HashMap<>();
        // 배너 목록을 contentId를 key로 하는 배열로 저장
        for (AdvertisementBannerDto dto : dtoList) {
            String contentId = String.valueOf(dto.getBannerId());
            AdvertisementBannerUseCase useCase = useCaseMap.get(contentId);
            if (useCase == null) {
                useCase = new AdvertisementBannerUseCase(contentId, dto.getThumbnailUrl(), dto.getDetailUrl(), new ArrayList<>());
                useCaseMap.put(contentId, useCase);
            }
            useCase.getContents().add(AdvertisementContent.from(dto));
        }
        return new ArrayList<>(useCaseMap.values());
    }

    /**
     * Banner 삭제하는 함수(isExposed를 false로) (공통 배너 삭제)
     *
     * @Param bannerId를 담은 useCase
     */
    @Transactional
    public void deleteBanner(DeleteUseCase deleteUseCase) {
        Long count = advertisementBannerRepository.updateIsExposedById(deleteUseCase.bannerId());
        // 삭제된 배너가 없을 경우
        if (count < 1) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
    }

    /**
     * 광고 배너 상세 페이지 조회 함수
     *
     * @Param bannerId, languageCose를 담은 useCase
     * @Return dto(id, title, content, detailImageUrl)
     */
    @Transactional(readOnly = true)
    public AdvertisementDetailDto findAdvertisementBanner(FindAdvertisementUseCase useCase) {
        AdvertisementDetailDto dto = advertisementBannerRepository.
            findAdvertisementBannerById(useCase.getBannerId(), useCase.getLanguageCode());
        // 조회된 배너가 없을 경우
        if (dto == null) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
        return dto;
    }

    /**
     * 광고 배너 썸네일 목록 조회 함수
     *
     * @Return dto(id, thumbnailImageUrl, title)
     */
    @Transactional(readOnly = true)
    public List<AdvertisementThumbnailDto> findThumbnailList(Long memberId) {
        return advertisementBannerRepository.findAdvertisementThumbnailList(memberId);
    }
}
