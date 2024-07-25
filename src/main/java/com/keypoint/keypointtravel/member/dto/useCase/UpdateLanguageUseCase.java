package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.member.dto.request.UpdatePasswordRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateLanguageUseCase {

    private Long memmberId;

    private LanguageCode language;

    public static UpdateLanguageUseCase of(Long memberId, UpdateLanguageRequest request) {
        return new UpdatePasswordUseCase(
            memberId,
            request.getLanguage()
        );
    }
}
