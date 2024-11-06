package com.keypoint.keypointtravel.member.repository.member;

import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.dto.response.AdminMemberResponse;
import com.keypoint.keypointtravel.member.dto.response.MemberSettingResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.response.otherMemberProfile.OtherMemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.useCase.AlarmMemberUserCase;
import com.keypoint.keypointtravel.member.dto.useCase.SearchAdminMemberUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.UpdateMemberUseCase;
import java.util.List;
import org.springframework.data.domain.Page;

public interface MemberCustomRepository {

    MemberProfileResponse findMemberProfile(Long memberId);

    OtherMemberProfileResponse findOtherMemberProfile(Long myId, Long otherMemberId);

    MemberSettingResponse findSettingByMemberId(Long memberId);

    List<MemberInfoDto> findCampaignMemberList(Long CampaignId);

    void updateMemberProfile(Long memberId, String name, Long profileImageId);

    void deleteMember(Long memberId);
    
    List<Long> findMemberIdsByLanguageCode(LanguageCode languageCode);

    List<AlarmMemberUserCase> findAlarmMembersByMemberIds(List<Long> memberIds);

    Page<AdminMemberResponse> findMembersInAdmin(SearchAdminMemberUseCase useCase);

    void updateMember(UpdateMemberUseCase useCase, String encodedPassword);
}
