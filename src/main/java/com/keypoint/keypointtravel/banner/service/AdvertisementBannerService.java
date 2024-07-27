package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.response.ImageUrlResponse;
import com.keypoint.keypointtravel.banner.dto.useCase.ImageUseCase;
import com.keypoint.keypointtravel.external.aws.service.S3Service;
import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AdvertisementBannerService {

    private final S3Service s3Service;

    /**
     * 이미지 파일을 url로 변환하는 함수(광고 배너 생성 시 마크다운 언어로 이미지를 넣을 수 있도록 하기 위함)
     *
     * @Param 이미지 파일
     *
     * @Return 이미지 파일의 url
     */
    public ImageUrlResponse uploadImage(ImageUseCase useCase) {
        try {
            String fileName = s3Service.uploadFileInS3(useCase.getImage(),
                DirectoryConstants.ADVERTISEMENT_BANNER_CONTENT_DIRECTORY);
            return new ImageUrlResponse(fileName);
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }
}
