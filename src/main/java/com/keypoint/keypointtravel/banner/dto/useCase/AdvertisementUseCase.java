package com.keypoint.keypointtravel.banner.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class AdvertisementUseCase {

    private MultipartFile thumbnailImage;
    private MultipartFile detailImage;
    private String mainTitle;
    private String subTitle;
    private String content;
}
