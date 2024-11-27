package com.keypoint.keypointtravel.requestPush.repository;

import com.keypoint.keypointtravel.requestPush.dto.useCase.UpdateRequestPushUseCase;
import com.keypoint.keypointtravel.requestPush.entity.QRequestPush;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

@RequiredArgsConstructor
public class RequestPushCustomRepositoryImpl implements RequestPushCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QRequestPush requestPush = QRequestPush.requestPush;

    @Override
    public long updateRequestPush(UpdateRequestPushUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        return queryFactory.update(requestPush)
            .set(requestPush.title, useCase.getTitle())
            .set(requestPush.content, useCase.getContent())
            .set(requestPush.languageCode, useCase.getLanguageCode())
            .set(requestPush.reservationAt, useCase.getReservationAt())
            .set(requestPush.roleType, useCase.getRoleType())
            .set(requestPush.modifyAt, LocalDateTime.now())
            .set(requestPush.modifyId, currentAuditor)
            .where(requestPush.id.eq(useCase.getRequestPushId()))
            .execute();
    }
}
