package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BannerErrorCode implements ErrorCode {

    REQUEST_DATA_MISMATCH("001_REQUEST_DATA_MISMATCH", "해당하는 타입이 존재하지 않습니다"),
    NOT_EXISTED_BANNER("002_NOT_EXISTED_BANNER", "존재하지 않는 배너입니다."),
    NOT_EXISTED_COMMENT("003_NOT_EXISTED_COMMENT", "댓글이 존재하지 않거나 작성자가 아닙니다."),
    NOT_EXISTED_LIKE("004_NOT_EXISTED_LIKE", "이미 좋아요를 취소하였거나 누르지 않았습니다."),
    NOT_EXISTED_TOURISM("005_NOT_EXISTED_TOURISM", "현재 위치 주변에 관광지가 존재하지 않습니다."),
    LANGUAGE_DATA_MISMATCH("006_LANGUAGE_DATA_MISMATCH", "해당하는 언어가 존재하지 않습니다"),
    EXISTS_BANNER_CONTENT("007_EXISTS_BANNER_CONTENT", "이미 해당 언어로 배너 내용이 존재합니다."),
    Exists_LIKE("008_Exists_LIKE", "이미 좋아요를 누른 상태입니다.");

    private final String code;
    private final String msg;
}
