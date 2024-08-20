package com.keypoint.keypointtravel.receipt.dto.useCase;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReceptImageUseCase {
    private MultipartFile receptImage;   
}
