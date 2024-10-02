package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.member.GenderType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.dto.request.MemberProfileRequest;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.entity.MemberDetail;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberProfileUseCase {

    private Long memberId;

    private String country;

    private GenderType gender;

    private LocalDate birth;

    private LanguageCode languageCode;

    public static MemberProfileUseCase of(Long memberId, MemberProfileRequest request) {
        return new MemberProfileUseCase(
            memberId,
            request.getCountry(),
            request.getGender(),
            request.getBirth(),
            request.getLanguageCode()
        );
    }

    public MemberDetail toEntity(Member member) {
        return new MemberDetail(
            member,
            this.gender,
            this.birth,
            this.languageCode,
            this.country
        );
    }
}
