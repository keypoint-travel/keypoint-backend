package com.keypoint.keypointtravel.campaign.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CampaignReportRepository {

    private final JPAQueryFactory queryFactory;

}
