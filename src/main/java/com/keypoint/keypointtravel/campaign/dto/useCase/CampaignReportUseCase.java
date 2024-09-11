package com.keypoint.keypointtravel.campaign.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class CampaignReportUseCase {

    private String email;
    private Long memberId;
    private Long campaignId;
    private MultipartFile reportImage;
}
