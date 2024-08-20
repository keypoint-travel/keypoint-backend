package com.keypoint.keypointtravel.campaign.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InviteFriendRequest {

    private Long campaignId;
    private List<MemberInfo> friends;
}
