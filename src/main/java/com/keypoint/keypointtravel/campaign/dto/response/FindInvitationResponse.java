package com.keypoint.keypointtravel.campaign.dto.response;

import com.keypoint.keypointtravel.friend.dto.FriendDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindInvitationResponse {

    private Long campaignId;
    private String title;
    private String invitationCode;
    private List<FriendDto> friends;
}
