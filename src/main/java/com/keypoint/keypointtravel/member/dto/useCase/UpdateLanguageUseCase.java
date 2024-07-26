package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.dto.request.UpdateLanguageRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateLanguageUseCase {

    private Long memberId;

    private LanguageCode language;

    public static UpdateLanguageUseCase of(Long memberId, UpdateLanguageRequest request) {
        return new UpdateLanguageUseCase(
            memberId,
            request.getLanguage()
        );
    }
}
