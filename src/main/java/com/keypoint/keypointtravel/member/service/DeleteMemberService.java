package com.keypoint.keypointtravel.member.service;

import com.keypoint.keypointtravel.auth.redis.service.RefreshTokenService;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeleteMemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;

    /**
     * 사용자 탈퇴시키는 함수
     * TODO 추후, 소셜 로그인 사용자일 때 연동 해제 시키는 로직 추가
     *
     * @param useCase
     */
    @Transactional
    public void deleteMember(MemberIdUseCase useCase) {
        try {
            CommonMemberDTO member = memberRepository.findByEmailAndIsDeletedFalse(
                    useCase.getEmail())
                .orElseThrow(() -> new GeneralException(MemberErrorCode.NOT_EXISTED_MEMBER));

            // 1. 탈퇴 처리
            memberRepository.deleteMember(useCase.getMemberId());

            // 2. 토큰 삭제
            refreshTokenService.deleteRefreshTokenByEmail(useCase.getEmail());

        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
