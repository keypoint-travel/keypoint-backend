package com.keypoint.keypointtravel.member.service;

import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import com.keypoint.keypointtravel.member.dto.request.SignUpRequest;
import com.keypoint.keypointtravel.member.dto.response.MemberDTO;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.MemberRepository;
import java.util.List;
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
        List<Member> members = memberRepository.findByEmail(email);
        if (members.isEmpty()) {
            throw new GeneralException(MemberErrorCode.NOT_EXISTED_EMAIL);
        }

        return members.get(0);
    }

    /**
     * Member 생성하는 함수 (일반 회원가입)
     *
     * @param request
     * @return
     */
    @Transactional
    public MemberDTO registerMember(SignUpRequest request) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();

            // 1. 이메일 유효성 검사
            validateEmailForSignUp(email);

            // 2. 비밀번호 유효성 검사
            if (!StringUtils.checkPasswordValidation(password)) {
                throw new GeneralException(MemberErrorCode.INVALID_PASSWORD);
            }

            // 3. Member 생성
            Member member = new Member(email, encodePassword(password), OauthProviderType.NONE);
            memberRepository.save(member);

            return MemberDTO.toDTO(member);
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
        List<Member> existMembers = memberRepository.findByEmail(email);
        if (!existMembers.isEmpty()) {
            Member existMember = existMembers.get(0);
            if (existMember.getEmail().contains(OauthProviderType.NONE.name())) {
                throw new GeneralException(MemberErrorCode.DUPLICATED_EMAIL);
            } else {
                throw new GeneralException(MemberErrorCode.REGISTERED_EMAIL_FOR_THE_OTHER);
            }
        }
    }
}
