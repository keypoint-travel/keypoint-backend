package com.keypoint.keypointtravel.campaign.dto.useCase;

import com.keypoint.keypointtravel.campaign.dto.request.createRequest.EmailInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InviteByEmailsUseCase {

    private List<String> emails;
    private Long memberId;
    private Long campaignId;

    public static InviteByEmailsUseCase of(List<EmailInfo> emails, Long memberId, Long campaignId) {
        return new InviteByEmailsUseCase(emails.stream().map(EmailInfo::getEmail).toList(),
            memberId, campaignId);
    }
}
