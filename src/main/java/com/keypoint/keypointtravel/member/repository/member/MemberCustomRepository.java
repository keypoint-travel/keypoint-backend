package com.keypoint.keypointtravel.member.repository.member;

import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.member.dto.response.MemberSettingResponse;
import com.keypoint.keypointtravel.member.dto.response.OtherMemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;

import java.util.List;

public interface MemberCustomRepository {

    MemberProfileResponse findMemberProfile(Long memberId);

    OtherMemberProfileResponse findOtherMemberProfile(Long myId, Long otherMemberId);

    MemberSettingResponse findSettingByMemberId(Long memberId);

    List<MemberInfoDto> findCampaignMemberList(Long CampaignId);
}
