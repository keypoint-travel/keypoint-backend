package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.notice.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NoticeCustomRepositoryImpl implements NoticeCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QNotice notice = QNotice.notice;
    private final QNoticeContent noticeContent = QNoticeContent.noticeContent;

    @Override
    public boolean isExistNoticeContentByLanguageCode(Long noticeId, LanguageCode languageCode) {
        NoticeContent content = queryFactory.selectFrom(noticeContent)
            .where(noticeContent.notice.id.eq(noticeId)
                .and(noticeContent.isDeleted.isFalse())
                .and(noticeContent.languageCode.eq(languageCode)))
            .fetchOne();

        return content != null;
    }
}
