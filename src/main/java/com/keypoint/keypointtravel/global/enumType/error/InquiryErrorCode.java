package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryErrorCode implements ErrorCode {
    TOO_MANY_IMAGES("001_TOO_MANY_IMAGES", "이미지는 최대 5개까지 등록 가능합니다."),
    NOT_EXISTED_INQUIRY("002_NOT_EXISTED_INQUIRY", "해당 문의 사항을 찾을 수 없습니다."),
    DELETED_INQUIRY("003_DELETED_INQUIRY", "삭제된 문의 사항입니다."),
    ALREADY_REPLIED_INQUIRY("004_ALREADY_REPLIED_INQUIRY", "이미 답변한 문의 사항입니다."),
    NOT_MATCHED_MEMBER("005_NOT_MATCHED_MEMBER", "문의한 회원이 아닙니다."),
    NOT_REPLIED_INQUIRY("006_NOT_REPLIED_INQUIRY", "아직 답변이 오지 않은 문의 사항입니다.");

    private final String code;
    private final String msg;
}
