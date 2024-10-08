package com.keypoint.keypointtravel.member.repository.member;

import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.dto.response.MemberSettingResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.response.otherMemberProfile.OtherMemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.useCase.AlarmMemberUserCase;
import java.util.List;

public interface MemberCustomRepository {

    MemberProfileResponse findMemberProfile(Long memberId);

    OtherMemberProfileResponse findOtherMemberProfile(Long myId, Long otherMemberId);

    MemberSettingResponse findSettingByMemberId(Long memberId);

    List<MemberInfoDto> findCampaignMemberList(Long CampaignId);

    void updateMemberProfile(Long memberId, String name, Long profileImageId);

    void deleteMember(Long memberId);
    
    List<Long> findMemberIdsByLanguageCode(LanguageCode languageCode);

    List<AlarmMemberUserCase> findAlarmMembersByMemberIds(List<Long> memberIds);
}
