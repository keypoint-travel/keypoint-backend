package com.keypoint.keypointtravel.inquiry.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteUseCase {

    private Long inquiryId;
    private Long memberId;
}
