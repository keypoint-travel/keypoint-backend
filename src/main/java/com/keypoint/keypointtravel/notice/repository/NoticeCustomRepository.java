package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageAndMemberIdUseCase;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.notice.dto.response.NoticeDetailResponse;
import com.keypoint.keypointtravel.notice.dto.response.NoticeResponse;
import com.keypoint.keypointtravel.notice.dto.response.adminNoticeDetail.AdminNoticeDetailResponse;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeContentUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeContentsUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.UpdateNoticeContentUseCase;
import org.springframework.data.domain.Page;

public interface NoticeCustomRepository {

    boolean isExistNoticeContentByLanguageCode(Long bannerId, LanguageCode languageCode);

    Page<NoticeResponse> findNotices(PageAndMemberIdUseCase useCase, LanguageCode languageCode);

    void updateNoticeContent(UpdateNoticeContentUseCase useCase);

    NoticeDetailResponse findNoticeByNoticeContentId(Long noticeContentId);

    void deleteNotices(DeleteNoticeUseCase useCase);

    void deleteNoticeContents(DeleteNoticeContentsUseCase useCase);

    long deleteNoticeContent(DeleteNoticeContentUseCase useCase);

    AdminNoticeDetailResponse findNoticeById(Long noticeId);
}
