package com.keypoint.keypointtravel.campaign.dto.useCase;

import java.sql.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmCampaignUseCase {

    private Long campaignId;
    private List<Long> memberIds;
    private Date startDate;
}
