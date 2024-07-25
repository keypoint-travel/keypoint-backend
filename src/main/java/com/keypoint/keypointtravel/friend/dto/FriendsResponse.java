package com.keypoint.keypointtravel.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FriendsResponse {

    private String invitationCode;
    private List<FriendDto> friends;

    public static FriendsResponse of(String invitationCode, List<FriendDto> friends) {
        return new FriendsResponse(invitationCode, friends);
    }
}
