package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.notice.dto.response.NoticeDetailResponse;
import com.keypoint.keypointtravel.notice.dto.response.NoticeResponse;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeContentUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.UpdateNoticeUseCase;
import org.springframework.data.domain.Page;

public interface NoticeCustomRepository {

    boolean isExistNoticeContentByLanguageCode(Long bannerId, LanguageCode languageCode);

    Page<NoticeResponse> findNotices(PageUseCase useCase);

    void updateNotice(UpdateNoticeUseCase useCase);

    NoticeDetailResponse findNoticeById(Long noticeContentId);

    void deleteNotices(DeleteNoticeUseCase useCase);

    void deleteNoticeContents(DeleteNoticeContentUseCase useCase);
}
