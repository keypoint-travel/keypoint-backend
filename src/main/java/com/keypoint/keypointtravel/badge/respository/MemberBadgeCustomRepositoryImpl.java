package com.keypoint.keypointtravel.badge.respository;

import com.keypoint.keypointtravel.badge.dto.response.badgeInMember.BadgeResponse;
import com.keypoint.keypointtravel.badge.dto.response.badgeInMember.RepresentativeBadgeResponse;
import com.keypoint.keypointtravel.badge.entity.QBadge;
import com.keypoint.keypointtravel.badge.entity.QEarnedBadge;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.member.dto.response.otherMemberProfile.EarnedBadgeResponse;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

@RequiredArgsConstructor
public class MemberBadgeCustomRepositoryImpl implements MemberBadgeCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QBadge badge = QBadge.badge;
    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;
    private final QUploadFile uploadFile = QUploadFile.uploadFile;
    private final QEarnedBadge earnedBadge = QEarnedBadge.earnedBadge;

    @Override
    public List<BadgeResponse> findBadgesByUserId(Long memberId) {
        BooleanBuilder badgeBuilder = new BooleanBuilder();
        badgeBuilder.and(badge.isDeleted.eq(false));

        BooleanBuilder earnedBadgeBuilder = new BooleanBuilder();
        earnedBadgeBuilder.and(earnedBadge.badge.id.eq(badge.id))
            .and(earnedBadge.member.id.eq(memberId));

        QUploadFile activeFile = new QUploadFile("activeFile");
        QUploadFile inactiveFile = new QUploadFile("inactiveFile");

        return queryFactory
            .select(
                Projections.constructor(
                    BadgeResponse.class,
                    badge,
                    earnedBadge,
                    activeFile.path.as("badgeOnImageUrl"),
                    inactiveFile.path.as("badgeOffImageUrl")
                )
            )
            .from(badge)
            .leftJoin(earnedBadge).on(earnedBadgeBuilder)
            .leftJoin(activeFile).on(activeFile.id.eq(badge.activeImageId))
            .leftJoin(inactiveFile).on(inactiveFile.id.eq(badge.inactiveImageId))
            .orderBy(badge.order.asc())
            .where(badgeBuilder)
            .fetch();
    }

    @Override
    public List<EarnedBadgeResponse> findEarnedBadgeByUserId(Long memberId) {
        return queryFactory.select(
                Projections.constructor(
                    EarnedBadgeResponse.class,
                    badge.id,
                    badge.name,
                    badge.order,
                    uploadFile.path))
            .from(badge)
            .innerJoin(earnedBadge).on(earnedBadge.badge.id.eq(badge.id).and(earnedBadge.member.id.eq(memberId)))
            .leftJoin(uploadFile).on(badge.activeImageId.eq(uploadFile.id))
            .where(badge.isDeleted.eq(false))
            .orderBy(badge.order.asc())
            .fetch();
    }

    @Override
    public RepresentativeBadgeResponse findRepresentativeBadgeByUserId(Long memberId) {
        return queryFactory
            .select(
                Projections.fields(
                    RepresentativeBadgeResponse.class,
                    memberDetail.representativeBadge.id.as("representativeBadgeId"),
                    uploadFile.path.as("badgeImageUrl")
                )
            )
            .from(memberDetail)
            .leftJoin(uploadFile).on(memberDetail.representativeBadge.id.eq(uploadFile.id))
            .where(memberDetail.member.id.eq(memberId))
            .fetchFirst();
    }
}
