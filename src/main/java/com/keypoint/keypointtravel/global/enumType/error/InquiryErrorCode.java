package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryErrorCode implements ErrorCode {
    TOO_MANY_IMAGES("001_TOO_MANY_IMAGES", "이미지는 최대 5개까지 등록 가능합니다."),
    NOT_EXISTED_INQUIRY("002_NOT_EXISTED_INQUIRY", "해당 문의 사항을 찾을 수 없습니다."),
    DELETED_OR_REPLIED_INQUIRY("003_DELETED_OR_REPLIED_INQUIRY", "삭제되었거나 답변이 완료된 문의 사항입니다.");

    private final String code;
    private final String msg;
}
