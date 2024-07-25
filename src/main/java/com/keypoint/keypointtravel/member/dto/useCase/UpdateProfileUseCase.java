package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.member.dto.request.UpdateProfileRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UpdateProfileUseCase {

    private Long memberId;

    private String name;

    private MultipartFile profileImage;

    public static UpdateProfileUseCase from(
        Long memberId,
        UpdateProfileRequest request,
        MultipartFile profileImage
    ) {
        return new UpdateProfileUseCase(
            memberId,
            request.getName(),
            profileImage);
    }
}
