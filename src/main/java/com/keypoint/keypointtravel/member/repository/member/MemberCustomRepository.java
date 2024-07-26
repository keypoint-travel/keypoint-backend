package com.keypoint.keypointtravel.member.repository.member;

import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;

public interface MemberCustomRepository {

    MemberProfileResponse findMemberProfile(Long memberId);
}
