package com.keypoint.keypointtravel.member.dto.request;

import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.enumType.member.GenderType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "이메일은 공백이 될 수 없습니다.")
    @Email(message = "올바른 형식의 이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.")
    private String password;

    private String country;

    @ValidEnum(enumClass = GenderType.class, nullable = true)
    private GenderType gender;

    private LocalDate birth;

    @NotBlank
    private String name;

    @ValidEnum(enumClass = LanguageCode.class)
    private LanguageCode languageCode;
}