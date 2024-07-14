package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.imageListUseCase.ImageListUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.TourismListUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.tourUseCase.TourismUseCase;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import org.springframework.stereotype.Component;

@Component
public class TourismApiServiceFallback implements TourismApiService {
    @Override
    public TourismListUseCase findTourismList(int pageNo, String serviceKey, String areaCode, String contentTypeId, String cat1, String cat2, String cat3) {
        throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL);
    }

    @Override
    public TourismUseCase findTourism(String contentId, String serviceKey) {
        throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL);
    }

    @Override
    public ImageListUseCase findImageList(String contentId, String serviceKey) {
        throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL);
    }
}
