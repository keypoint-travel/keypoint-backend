package com.keypoint.keypointtravel.premium.repository;

import com.keypoint.keypointtravel.premium.entity.MemberPremium;
import com.keypoint.keypointtravel.premium.entity.QMemberPremium;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomPremiumRepositoryImpl implements CustomPremiumRepository {

    private final JPAQueryFactory queryFactory;

    private final QMemberPremium memberPremium = QMemberPremium.memberPremium;

    @Override
    public List<MemberPremium> findExpiredMemberPremiums() {
        return queryFactory.selectFrom(memberPremium)
            .where(memberPremium.isActive.and(
                memberPremium.expirationAt.lt(LocalDateTime.now())))
            .fetch();
    }
}
