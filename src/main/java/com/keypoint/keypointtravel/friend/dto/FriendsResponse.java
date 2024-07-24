package com.keypoint.keypointtravel.friend.dto;

import com.keypoint.keypointtravel.friend.entity.Friend;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FriendsResponse {

    private String invitationCode;
    private List<FriendInfo> friends;

    public static FriendsResponse of(String invitationCode, List<Friend> friends) {
        return new FriendsResponse(invitationCode, friends.stream().map(FriendInfo::from).toList());
    }
}
