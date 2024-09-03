package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.notice.dto.response.NoticesResponse;
import org.springframework.data.domain.Page;

public interface NoticeCustomRepository {

    boolean isExistNoticeContentByLanguageCode(Long bannerId, LanguageCode languageCode);

    Page<NoticesResponse> findNotices(PageUseCase useCase);

}
