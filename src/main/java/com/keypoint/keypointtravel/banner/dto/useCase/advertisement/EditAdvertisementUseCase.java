package com.keypoint.keypointtravel.banner.dto.useCase.advertisement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class EditAdvertisementUseCase {

    private MultipartFile thumbnailImage;
    private MultipartFile detailImage;
}
