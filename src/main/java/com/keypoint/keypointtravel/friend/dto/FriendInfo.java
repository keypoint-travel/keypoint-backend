package com.keypoint.keypointtravel.friend.dto;

import com.keypoint.keypointtravel.friend.entity.Friend;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendInfo {

    private Long friendId;
    private String friendName;
    private Long profileImageId;

    public static FriendInfo from(Friend friend){
        return new FriendInfo(friend.getFriendId(), friend.getFriendName(), friend.getProfileImageId());
    }
}
