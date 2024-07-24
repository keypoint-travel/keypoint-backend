package com.keypoint.keypointtravel.friend.repository;

import com.keypoint.keypointtravel.member.entity.Member;

import java.util.Optional;

public interface FriendCustomRepository {

    Member findMemberByEmailOrInvitationCode(String invitationValue);
}
