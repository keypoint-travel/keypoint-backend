package com.keypoint.keypointtravel.member.service;

import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import com.keypoint.keypointtravel.member.dto.response.MemberDTO;
import com.keypoint.keypointtravel.member.dto.useCase.SignUpUseCase;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * email로 Member를 조회하는 함수
     *
     * @param email
     * @return
     */
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new GeneralException(MemberErrorCode.NOT_EXISTED_EMAIL));
    }

    /**
     * Member 생성하는 함수 (일반 회원가입)
     *
     * @param useCase
     * @return
     */
    @Transactional
    public MemberDTO registerMember(SignUpUseCase useCase) {
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

            return MemberDTO.from(member);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * (일반 회원가입 경우) 계정 유효성 검사
     *
     * @param email
     */
    public void validateEmailForSignUp(String email) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            Member existMember = memberOptional.get();
            if (existMember.getEmail().contains(OauthProviderType.NONE.name())) {
                throw new GeneralException(MemberErrorCode.DUPLICATED_EMAIL);
            } else {
                throw new GeneralException(MemberErrorCode.REGISTERED_EMAIL_FOR_THE_OTHER);
            }
        }
    }
}
