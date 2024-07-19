package com.keypoint.keypointtravel.member.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keypoint.keypointtravel.global.enumType.email.EmailTemplate;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.EmailUtils;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.dto.response.MemberResponse;
import com.keypoint.keypointtravel.member.dto.useCase.EmailUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.SignUpUseCase;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreateMemberService {
    private static final int EMAIL_VERIFICATION_CODE_DIGITS = 6;

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Member 생성하는 함수 (일반 회원가입)
     *
     * @param useCase 회원 가입 데이터
     * @return
     */
    @Transactional
    public MemberResponse registerMember(SignUpUseCase useCase) {
        try {
            String email = useCase.getEmail();
            String password = useCase.getPassword();

            // 1. 이메일 유효성 검사
            validateEmailForSignUp(email);

            // 2. 비밀번호 유효성 검사
            if (!StringUtils.checkPasswordValidation(password)) {
                throw new GeneralException(MemberErrorCode.INVALID_PASSWORD);
            }

            // 3. Member 생성
            Member member = new Member(email, encodePassword(password), OauthProviderType.NONE);
            memberRepository.save(member);

            return MemberResponse.from(member);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 비밀번호를 PasswordEncoder로 암호화
     * @param password 암호화할 비밀번호
     * @return
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * (일반 회원가입 경우) 계정 유효성 검사
     *
     * @param email 이메일
     */
    private void validateEmailForSignUp(String email) {
        Optional<CommonMemberDTO> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            CommonMemberDTO existMember = memberOptional.get();
            if ( existMember.getOauthProviderType() == OauthProviderType.NONE) {
                throw new GeneralException(MemberErrorCode.DUPLICATED_EMAIL);
            } else {
                throw new GeneralException(MemberErrorCode.REGISTERED_EMAIL_FOR_THE_OTHER);
            }
        }
    }

    /**
     * 이메일 인증 코드 전달 함수
     * @param useCase 이메일 정보 데이터
     * @return
     */
    public boolean sendVerificationCodeToEmail(EmailUseCase useCase) {
        String code = StringUtils.getRandomNumber(EMAIL_VERIFICATION_CODE_DIGITS);
        boolean result = EmailUtils.sendEmail(useCase.getEmail(), EmailTemplate.EMAIL_VERIFICATION, );
        return result;
    }
}
