package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PremiumErrorCode implements ErrorCode {

    INCORRECT_RECEIPT("001_INCORRECT_RECEIPT", "결제 정보가 올바르지 않습니다."),
    NOT_FOUND_MEMBER_PREMIUM("002_NOT_FOUND_MEMBER_PREMIUM", "프리미엄 회원 정보가 존재하지 않습니다."),
    ALREADY_EXIST_MEMBER_PREMIUM("003_ALREADY_EXIST_MEMBER_PREMIUM", "이미 프리미엄 사용 이력이 존재하여 7일간 무료체험을 할 수 없습니다.");
    private final String code;
    private final String msg;
}
