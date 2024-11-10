package com.keypoint.keypointtravel.inquiry.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindUserInquiryUseCase {

    private Long inquiryId;
    private Long memberId;
}
