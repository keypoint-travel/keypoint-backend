package com.keypoint.keypointtravel.campaign.dto.useCase;

import com.keypoint.keypointtravel.campaign.dto.request.MemberInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InviteFriendUseCase {

    private Long campaignId;
    private Long memberId;
    private List<Long> friendIds;

    static public InviteFriendUseCase of(Long campaignId, Long memberId, List<MemberInfo> friends) {
        return new InviteFriendUseCase(
            campaignId,
            memberId,
            friends.stream().map(MemberInfo::getMemberId).toList()
        );
    }
}
