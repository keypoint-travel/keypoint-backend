package com.keypoint.keypointtravel.member.repository.member;

import static com.querydsl.jpa.JPAExpressions.selectOne;

import com.keypoint.keypointtravel.badge.entity.QEarnedBadge;
import com.keypoint.keypointtravel.blocked_member.entity.QBlockedMember;
import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.campaign.entity.QMemberCampaign;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.member.GenderType;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.dto.response.AdminMemberResponse;
import com.keypoint.keypointtravel.member.dto.response.MemberSettingResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberAlarmResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.response.otherMemberProfile.OtherMemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.useCase.AlarmMemberUserCase;
import com.keypoint.keypointtravel.member.dto.useCase.SearchAdminMemberUseCase;
import com.keypoint.keypointtravel.member.entity.QMember;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.keypoint.keypointtravel.premium.entity.QMemberPremium;
import com.keypoint.keypointtravel.theme.dto.response.MemberThemeResponse;
import com.keypoint.keypointtravel.theme.entity.QPaidTheme;
import com.keypoint.keypointtravel.theme.entity.QTheme;
import com.keypoint.keypointtravel.theme.entity.QThemeColor;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
                    checkIsBlocked(myId, otherMemberId).as("isBlocked")
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

    private BooleanExpression checkIsBlocked(Long myId, Long otherMemberId) {
        return selectOne()
            .from(QBlockedMember.blockedMember)
            .where(blockedMember.member.id.eq(myId)
                .and(blockedMember.blockedMemberId.eq(otherMemberId)))
            .exists();
    }

    @Override
    public Page<AdminMemberResponse> findMembersInAdmin(SearchAdminMemberUseCase useCase) {
        Pageable pageable = useCase.getPageable();
        String sortBy = useCase.getSortBy();
        String direction = useCase.getDirection();
        String keyword = useCase.getKeyword();
        RoleType role = useCase.getRole();

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(member.isDeleted.isFalse());

        // 검색어 필터링
        if (role != null) {
            builder.and(member.role.eq(role));
        }
        if (keyword != null && !keyword.isBlank()) {
            builder.and(
                member.id.stringValue().containsIgnoreCase(keyword)
                    .or(member.name.containsIgnoreCase(keyword))
                    .or(memberDetail.country.containsIgnoreCase(keyword))
                    .or(GenderType.containsEnumValue(memberDetail.gender, keyword))
                    .or(memberDetail.birth.stringValue().contains(keyword))
                    .or(member.email.containsIgnoreCase(keyword))
                    .or(RoleType.containsEnumValue(member.role, keyword))
                    .or(member.createAt.stringValue().contains(keyword))
            );
        }

        // 기본 정렬 기준 추가
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.ASC, member.id);

        // 동적 정렬 기준 처리
        if (sortBy != null) {
            Order order = direction.equals("asc") ? Order.ASC : Order.DESC;

            switch (sortBy) {
                case "userId":
                    orderSpecifier = new OrderSpecifier<>(order, member.id);
                    break;
                case "name":
                    orderSpecifier = new OrderSpecifier<>(order, member.name);
                    break;
                case "country":
                    orderSpecifier = new OrderSpecifier<>(order, memberDetail.country);
                    break;
                case "gender":
                    orderSpecifier = new OrderSpecifier<>(order, GenderType.getOrderSpecifier());
                    break;
                case "birth":
                    orderSpecifier = new OrderSpecifier<>(order, memberDetail.birth);
                    break;
                case "email":
                    orderSpecifier = new OrderSpecifier<>(order, member.email);
                    break;
                case "role":
                    orderSpecifier = new OrderSpecifier<>(order, RoleType.getOrderSpecifier());
                    break;
                case "recentRegisterReceiptAt":
                    orderSpecifier = new OrderSpecifier<>(order,
                        memberDetail.recentRegisterReceiptAt);
                    break;
            }
        }

        List<AdminMemberResponse> data = queryFactory
            .select(
                Projections.constructor(
                    AdminMemberResponse.class,
                    member.id,
                    uploadFile.path,
                    member.name,
                    memberDetail.country,
                    memberDetail.gender,
                    memberDetail.birth,
                    member.email,
                    member.role,
                    memberDetail.recentRegisterReceiptAt
                )
            )
            .from(member)
            .innerJoin(member.memberDetail, memberDetail)
            .leftJoin(uploadFile).on(uploadFile.id.eq(memberDetail.profileImageId))
            .where(builder)
            .orderBy(orderSpecifier, member.id.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = queryFactory
            .select(
                member.count()
            )
            .where(builder)
            .from(member)
            .fetchOne();

        return new PageImpl<>(data, pageable, count);
    }

}