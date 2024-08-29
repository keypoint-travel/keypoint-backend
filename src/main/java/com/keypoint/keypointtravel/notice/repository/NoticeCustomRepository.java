package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;

public interface NoticeCustomRepository {

    boolean isExistNoticeContentByLanguageCode(Long bannerId, LanguageCode languageCode);

}
