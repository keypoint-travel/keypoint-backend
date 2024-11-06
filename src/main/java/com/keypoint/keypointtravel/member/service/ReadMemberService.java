package com.keypoint.keypointtravel.member.service;

import com.keypoint.keypointtravel.badge.respository.BadgeRepository;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.dto.response.AdminMemberResponse;
import com.keypoint.keypointtravel.member.dto.response.MemberSettingResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.response.otherMemberProfile.OtherMemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.useCase.EmailUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.OtherMemberUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.SearchAdminMemberUseCase;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadMemberService {

    private final MemberRepository memberRepository;

    private final BadgeRepository badgeRepository;

    /**
     * email로 Member의 인증에 필요한 정보를 조회하는 함수
     *
     * @param email Member를 조회할 이메일
     * @return
     */
    public CommonMemberDTO findMemberByEmail(String email) {
        return memberRepository.findByEmailAndIsDeletedFalse(email)
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
     * 등록되지 않은 이메일인지 확인하는 함수
     *
     * @param useCase 확인할 이메일 정보
     * @return 이메일 등록 여부
     */
    public boolean checkIsNotExistedEmail(EmailUseCase useCase) {
        try {
            boolean isExisted = memberRepository.existsByEmailAndIsDeletedFalse(useCase.getEmail());
            if (isExisted) { // 등록된 이메일인 경우 에러 반환
                throw new GeneralException(MemberErrorCode.DUPLICATED_EMAIL);
            }

            return isExisted;
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
            MemberProfileResponse response = memberRepository.findMemberProfile(
                useCase.getMemberId()
            );
            if (response == null) {
                throw new GeneralException(MemberErrorCode.NOT_EXISTED_MEMBER);
            }

            return response;
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
            OtherMemberProfileResponse response = memberRepository.findOtherMemberProfile(useCase.getMyId(),
                useCase.getOtherMemberId());
            response.addBadges(badgeRepository.findEarnedBadgeByUserId(useCase.getOtherMemberId()));
            return response;
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 사용자 설정 정보를 조회하는 정보
     *
     * @param useCase 사용자 아이디 데이터
     * @return
     */
    public MemberSettingResponse getMemberSetting(MemberIdUseCase useCase) {
        try {
            return memberRepository.findSettingByMemberId(useCase.getMemberId());
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 유저 관리 목록 조회 함수
     *
     * @param useCase
     * @return
     */
    public Page<AdminMemberResponse> findMembersInAdmin(SearchAdminMemberUseCase useCase) {
        try {
            return memberRepository.findMembersInAdmin(useCase);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }
}
