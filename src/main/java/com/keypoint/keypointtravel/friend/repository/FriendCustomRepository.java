package com.keypoint.keypointtravel.friend.repository;

import com.keypoint.keypointtravel.friend.dto.FriendDto;
import com.keypoint.keypointtravel.member.entity.Member;

import java.util.List;

public interface FriendCustomRepository {

    Member findMemberByEmailOrInvitationCode(String invitationValue);

    long updateIsDeletedById(Long memberId, Long friendId);

    List<FriendDto> findAllByMemberId(Long memberId);
}
