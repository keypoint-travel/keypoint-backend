package com.keypoint.keypointtravel.member.service;

import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.dto.response.OtherMemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.useCase.EmailUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.OtherMemberUseCase;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadMemberService {

    private final MemberRepository memberRepository;

    /**
     * email로 Member의 인증에 필요한 정보를 조회하는 함수
     *
     * @param email Member를 조회할 이메일
     * @return
     */
    public CommonMemberDTO findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new GeneralException(MemberErrorCode.NOT_EXISTED_EMAIL));
    }

    /**
     * memberId로 Member 조회하는 함수
     *
     * @param memberId Member를 조회할 memberId
     * @return
     */
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new GeneralException(MemberErrorCode.NOT_EXISTED_MEMBER));
    }


    /**
     * 등록된 이메일인지 확인하는 함수
     *
     * @param useCase 확인할 이메일 정보
     * @return 이메일 등록 여부
     */
    public boolean checkIsExistedEmail(EmailUseCase useCase) {
        try {
            return memberRepository.existsByEmail(useCase.getEmail());
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * Member profile 정보를 조회하는 함수
     *
     * @param useCase
     * @return
     */
    public MemberProfileResponse getMemberProfile(MemberIdUseCase useCase) {
        try {
            return memberRepository.findMemberProfile(useCase.getMemberId());
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 다른 회원 profile 정보를 조회하는 함수
     *
     * @param useCase myId, 다른 회원의 memberId
     * @return 다른 회원의 profile 정보
     */
    public OtherMemberProfileResponse getOtherMemberProfile(OtherMemberUseCase useCase) {
        try {
            return memberRepository.findOtherMemberProfile(useCase.getMyId(),
                useCase.getOtherMemberId());
            // todo: 다른 회원의 프로필 조회 시, 해당 회원이 가지고 있는 배지 정보도 함께 조회하기 구현 필요
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
