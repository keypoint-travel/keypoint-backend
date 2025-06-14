package com.keypoint.keypointtravel.member.repository.member;

import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.dto.response.MemberSettingResponse;
import com.keypoint.keypointtravel.member.dto.response.StatisticResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.response.otherMemberProfile.OtherMemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.useCase.AlarmMemberUserCase;
import java.time.LocalDateTime;
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

    List<StatisticResponse> findMonthlyLoginStatistics(LocalDateTime startAt, LocalDateTime endAt);

    List<StatisticResponse> findMonthlySignUpStatistics(LocalDateTime startAt, LocalDateTime endAt);

    List<StatisticResponse> findDailyVisitorsStatistics(LocalDateTime startAt, LocalDateTime endAt);

    List<Long> findMemberIdByLanguageAndRole(LanguageCode languageCode, RoleType roleType);

    List<String> findEmailByLanguageAndRole(LanguageCode languageCode, RoleType roleType);
}
