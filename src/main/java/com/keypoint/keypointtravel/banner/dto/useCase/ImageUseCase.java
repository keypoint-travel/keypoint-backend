package com.keypoint.keypointtravel.banner.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ImageUseCase {

    private MultipartFile image;
}
