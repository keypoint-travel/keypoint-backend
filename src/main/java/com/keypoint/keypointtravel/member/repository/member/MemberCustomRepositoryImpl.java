package com.keypoint.keypointtravel.member.repository.member;

import static com.querydsl.jpa.JPAExpressions.selectOne;

import com.keypoint.keypointtravel.badge.entity.QEarnedBadge;
import com.keypoint.keypointtravel.blocked_member.entity.QBlockedMember;
import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.campaign.entity.QMemberCampaign;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.dto.response.MemberSettingResponse;
import com.keypoint.keypointtravel.member.dto.response.StatisticResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberAlarmResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.response.otherMemberProfile.OtherMemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.useCase.AlarmMemberUserCase;
import com.keypoint.keypointtravel.member.entity.QMember;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.keypoint.keypointtravel.premium.entity.QMemberPremium;
import com.keypoint.keypointtravel.theme.dto.response.MemberThemeResponse;
import com.keypoint.keypointtravel.theme.entity.QPaidTheme;
import com.keypoint.keypointtravel.theme.entity.QTheme;
import com.keypoint.keypointtravel.theme.entity.QThemeColor;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QMember member = QMember.member;
    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;
    private final QUploadFile uploadFile = QUploadFile.uploadFile;
    private final QEarnedBadge earnedBadge = QEarnedBadge.earnedBadge;
    private final QMemberPremium memberPremium = QMemberPremium.memberPremium;
    private final QBlockedMember blockedMember = QBlockedMember.blockedMember;
    private final QMemberCampaign memberCampaign = QMemberCampaign.memberCampaign;

    private final QTheme theme = QTheme.theme;
    private final QPaidTheme paidTheme = QPaidTheme.paidTheme;
    private final QThemeColor themeColor = QThemeColor.themeColor;

    @Override
    public MemberProfileResponse findMemberProfile(Long memberId) {
        QUploadFile badgeFile = new QUploadFile("badgeFile");

        MemberProfileResponse profileResponse = queryFactory
            .select(
                Projections.constructor(
                    MemberProfileResponse.class,
                    member.id,
                    member.name,
                    member.email,
                    uploadFile.path,
                    memberDetail.language,
                    Projections.fields(
                        MemberAlarmResponse.class,
                        member.notification.pushNotificationEnabled,
                        member.notification.marketingNotificationEnabled
                    ),
                    earnedBadge.count(),
                    memberCampaign.count(),
                    badgeFile.path,
                    memberPremium.count(),
                    // 테마 정보 추가
                    Projections.constructor(
                        MemberThemeResponse.class,
                        new CaseBuilder()
                            .when(paidTheme.isNotNull())
                            .then(paidTheme.color)
                            .otherwise(theme.color).as("color"),
                        new CaseBuilder()
                            .when(paidTheme.isNotNull())
                            .then(paidTheme.name)
                            .otherwise(theme.name).as("name")
                    )
                )
            )
            .from(member)
            .leftJoin(uploadFile).on(uploadFile.id.eq(member.memberDetail.profileImageId))
            .innerJoin(member.memberDetail, memberDetail)
            .leftJoin(badgeFile).on(memberDetail.representativeBadge.id.eq(badgeFile.id))
            .leftJoin(memberPremium).on(memberPremium.member.id.eq(memberId))
            .leftJoin(earnedBadge).on(earnedBadge.member.id.eq(memberId))
            .leftJoin(memberCampaign).on(memberCampaign.member.id.eq(memberId))
            .leftJoin(theme).on(memberDetail.theme.eq(theme))
            .leftJoin(paidTheme).on(memberDetail.paidTheme.eq(paidTheme))
            .where(member.id.eq(memberId))
            .fetchOne();

        // chartColors 가져오기
        List<String> chartColors = queryFactory
            .select(themeColor.color)
            .from(themeColor)
            .leftJoin(memberDetail).on(memberDetail.member.id.eq(memberId))
            .leftJoin(paidTheme).on(memberDetail.paidTheme.eq(paidTheme))
            .leftJoin(theme).on(memberDetail.theme.eq(theme))
            .where(
                memberDetail.paidTheme.isNotNull()
                    .and(themeColor.paidTheme.eq(paidTheme))
                    .or(memberDetail.paidTheme.isNull()
                        .and(themeColor.theme.eq(theme)))
            )
            .fetch();

        // 응답에 테마 chartColors 추가
        if (profileResponse != null && profileResponse.getThemes() != null) {
            profileResponse.getThemes().withChartColors(chartColors);
        }

        return profileResponse;
    }

    @Override
    public OtherMemberProfileResponse findOtherMemberProfile(Long myId, Long otherMemberId) {
        return queryFactory.select(
                Projections.constructor(
                    OtherMemberProfileResponse.class,
                    member.id.as("memberId"),
                    uploadFile.path.as("profileImageUrl"),
                    member.name.as("memberName"),
                    isBlocked(myId, otherMemberId).as("isBlocked")
                )
            )
            .from(member)
            .leftJoin(uploadFile).on(uploadFile.id.eq(member.memberDetail.profileImageId))
            .where(member.id.eq(otherMemberId))
            .fetchOne();
    }

    @Override
    public MemberSettingResponse findSettingByMemberId(Long memberId) {
        return queryFactory
            .select(
                Projections.fields(
                    MemberSettingResponse.class,
                    member.memberDetail.language,
                    member.notification.pushNotificationEnabled
                )
            )
            .from(member)
            .where(member.id.eq(memberId))
            .fetchOne();
    }

    @Override
    public List<MemberInfoDto> findCampaignMemberList(Long campaignId) {
        return queryFactory.select(
                Projections.constructor(
                    MemberInfoDto.class,
                    member.id,
                    uploadFile.path,
                    member.name))
            .from(member)
            .innerJoin(memberCampaign).on(memberCampaign.member.id.eq(member.id))
            .leftJoin(uploadFile).on(uploadFile.id.eq(member.memberDetail.profileImageId))
            .where(memberCampaign.campaign.id.eq(campaignId))
            .fetch();
    }

    @Override
    public void updateMemberProfile(Long memberId, String name, Long profileImageId) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory.update(member)
            .set(member.name, name)
            .set(member.modifyAt, LocalDateTime.now())
            .set(member.modifyId, currentAuditor)
            .where(member.id.eq(memberId))
            .execute();

        queryFactory.update(memberDetail)
            .set(memberDetail.profileImageId, profileImageId)
            .set(memberDetail.modifyAt, LocalDateTime.now())
            .set(memberDetail.modifyId, currentAuditor)
            .where(memberDetail.member.id.eq(memberId))
            .execute();
    }

    @Override
    public void deleteMember(Long memberId) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory.update(member)
            .set(member.isDeleted, true)
            .set(member.role, RoleType.ROLE_PENDING_WITHDRAWAL)

            .set(member.modifyAt, LocalDateTime.now())
            .set(member.modifyId, currentAuditor)

            .where(member.id.eq(memberId))
            .execute();
    }

    @Override
    public List<Long> findMemberIdsByLanguageCode(LanguageCode languageCode) {
        return queryFactory
            .select(member.id)
            .from(member)
            .innerJoin(memberDetail)
            .on(memberDetail.language.eq(languageCode).and(memberDetail.member.eq(member)))
            .where(member.isDeleted.isFalse())
            .fetch();
    }

    @Override
    public List<AlarmMemberUserCase> findAlarmMembersByMemberIds(List<Long> memberIds) {
        return queryFactory.select(
                Projections.fields(
                    AlarmMemberUserCase.class,
                    member.id.as("memberId"),
                    member.name,
                    member.memberDetail.language,
                    member.notification.pushNotificationEnabled
                )
            )
            .from(member)
            .where(member.id.in(memberIds))
            .fetch();
    }

    @Override
    public List<StatisticResponse> findMonthlyLoginStatistics(
        LocalDateTime startAt,
        LocalDateTime endAt
    ) {
        return queryFactory
            .select(
                Projections.fields(
                    StatisticResponse.class,
                    Expressions.stringTemplate(
                        "DATE_FORMAT({0}, {1})",
                        member.recentLoginAt,
                        Expressions.constant("%Y/%m")).as("date"),
                    member.id.count().as("value")
                )
            )
            .from(member)
            .where(member.recentLoginAt.isNotNull()
                .and(member.recentLoginAt.goe(startAt))
                .and(member.recentLoginAt.lt(endAt))
            )
            .groupBy(Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                member.recentLoginAt,
                Expressions.constant("%Y/%m")
            ))
            .orderBy(Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                member.recentLoginAt,
                Expressions.constant("%Y/%m")
            ).asc()).fetch();
    }

    @Override
    public List<StatisticResponse> findMonthlySignUpStatistics(
        LocalDateTime startAt,
        LocalDateTime endAt
    ) {
        return queryFactory
            .select(
                Projections.fields(
                    StatisticResponse.class,
                    Expressions.stringTemplate(
                        "DATE_FORMAT({0}, {1})",
                        member.createAt,
                        Expressions.constant("%Y/%m")).as("date"),
                    member.id.count().as("value")
                )
            )
            .from(member)
            .where(member.createAt.isNotNull()
                .and(member.role.ne(RoleType.ROLE_UNCERTIFIED_USER))
                .and(member.role.ne(RoleType.ROLE_ADMIN))
                .and(member.createAt.goe(startAt))
                .and(member.createAt.lt(endAt))
            )
            .groupBy(Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                member.createAt,
                Expressions.constant("%Y/%m")
            ))
            .orderBy(Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                member.createAt,
                Expressions.constant("%Y/%m")
            ).asc()).fetch();
    }

    @Override
    public List<StatisticResponse> findDailyVisitorsStatistics(LocalDateTime startAt,
        LocalDateTime endAt) {
        return queryFactory
            .select(
                Projections.fields(
                    StatisticResponse.class,
                    Expressions.stringTemplate(
                        "DATE_FORMAT({0}, {1})",
                        member.recentLoginAt,
                        Expressions.constant("%Y/%m/%d")).as("date"),
                    member.id.count().as("value")
                )
            )
            .from(member)
            .where(member.recentLoginAt.isNotNull()
                .and(member.recentLoginAt.goe(startAt))
                .and(member.recentLoginAt.lt(endAt))
            )
            .groupBy(Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                member.recentLoginAt,
                Expressions.constant("%Y/%m/%d")
            ))
            .orderBy(Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                member.recentLoginAt,
                Expressions.constant("%Y/%m/%d")
            ).asc()).fetch();
    }

    @Override
    public List<Long> findMemberIdByLanguageAndRole(
        LanguageCode languageCode,
        RoleType roleType
    ) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(member.role.ne(RoleType.ROLE_ADMIN)
            .and(member.role.ne(RoleType.ROLE_UNCERTIFIED_USER)));

        // 조건으로 필터링
        if (languageCode != null) {
            booleanBuilder.and(member.memberDetail.language.eq(languageCode));
        }
        if (roleType != null) {
            booleanBuilder.and(member.role.eq(roleType));
        }

        // 조회
        return queryFactory
            .select(member.id)
            .from(member)
            .where(booleanBuilder)
            .fetch();

    }

    @Override
    public List<String> findEmailByLanguageAndRole(
        LanguageCode languageCode,
        RoleType roleType
    ) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(member.isDeleted.isFalse())
            .and(member.role.ne(RoleType.ROLE_ADMIN)
                .and(member.role.ne(RoleType.ROLE_UNCERTIFIED_USER)));

        // 조건으로 필터링
        if (languageCode != null) {
            booleanBuilder.and(member.memberDetail.language.eq(languageCode));
        }
        if (roleType != null) {
            booleanBuilder.and(member.role.eq(roleType));
        }

        // 조회
        return queryFactory
            .select(member.email)
            .from(member)
            .where(booleanBuilder)
            .fetch();

    }

    private BooleanExpression isBlocked(Long myId, Long otherMemberId) {
        return selectOne()
            .from(QBlockedMember.blockedMember)
            .where(blockedMember.member.id.eq(myId)
                .and(blockedMember.blockedMemberId.eq(otherMemberId)))
            .exists();
    }
}