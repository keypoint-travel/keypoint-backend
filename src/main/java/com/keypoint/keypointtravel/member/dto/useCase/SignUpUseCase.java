package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.member.GenderType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.dto.request.SignUpRequest;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.entity.MemberDetail;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpUseCase {

    private String email;

    private String password;

    private String country;

    private GenderType gender;

    private LocalDate birth;

    private String name;

    private LanguageCode languageCode;

    public static SignUpUseCase from(SignUpRequest request) {
        return new SignUpUseCase(
            request.getEmail(),
            request.getPassword(),
            request.getCountry(),
            request.getGender(),
            request.getBirth(),
            request.getName(),
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
