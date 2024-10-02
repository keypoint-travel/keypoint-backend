package com.keypoint.keypointtravel.campaign.repository;

import static com.querydsl.jpa.JPAExpressions.select;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignReportDto;
import com.keypoint.keypointtravel.campaign.entity.QCampaignReport;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomCampaignReportRepository {

    private final JPAQueryFactory queryFactory;

    private final QCampaignReport campaignReport = QCampaignReport.campaignReport;

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;

    // 캠페인 레포트가 존재하는지 확인
    public boolean existsByCampaignId(Long campaignId) {
        return queryFactory.selectOne()
            .from(campaignReport)
            .where(campaignReport.campaign.id.eq(campaignId))
            .fetchFirst() != null;
    }

    // 캠페인 레포트 이미지 url 반환
    public String findReportImageUrl(Long campaignId) {
        return queryFactory.select(uploadFile.path)
            .from(campaignReport)
            .innerJoin(uploadFile).on(campaignReport.reportImageId.eq(uploadFile.id))
            .where(campaignReport.campaign.id.eq(campaignId))
            .fetchFirst();
    }

    // 메일 전송에 포함될 캠페인 정보 반환
    public CampaignReportDto findCampaignReportInfo(Long campaignId, Long memberId) {
        return queryFactory.select(Projections.constructor(CampaignReportDto.class,
                    campaignReport.campaign.title.as("campaignName"),
                    ExpressionUtils.as(
                        select(memberDetail.member.name).from(memberDetail)
                            .where(memberDetail.member.id.eq(memberId)), "memberName")
                )
            )
            .from(campaignReport)
            .where(campaignReport.campaign.id.eq(campaignId))
            .fetchOne();
    }
}
