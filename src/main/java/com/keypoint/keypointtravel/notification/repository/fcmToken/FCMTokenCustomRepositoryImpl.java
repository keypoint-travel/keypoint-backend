package com.keypoint.keypointtravel.notification.repository.fcmToken;

import com.keypoint.keypointtravel.notification.dto.useCase.FCMTokenUseCase;
import com.keypoint.keypointtravel.notification.entity.QFCMToken;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FCMTokenCustomRepositoryImpl implements FCMTokenCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QFCMToken fcmToken = QFCMToken.fCMToken;


    @Override
    public long deleteFCMTokenByTokenAndMemberId(FCMTokenUseCase useCase) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(fcmToken.token.eq(useCase.getFcmToken()))
            .and(fcmToken.member.id.eq(useCase.getMemberId()));

        return queryFactory.delete(fcmToken)
            .where(builder)
            .execute();
    }

    @Override
    @Transactional
    public long deleteFCMTokenByTokens(List<String> tokens) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(fcmToken.token.in(tokens));

        return queryFactory.delete(fcmToken)
            .where(builder)
            .execute();
    }
}
