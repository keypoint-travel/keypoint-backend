package com.keypoint.keypointtravel.member.service;

import com.keypoint.keypointtravel.global.exception.GeneralException;
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

    /**
     * 사용자 탈퇴시키는 함수
     * TODO 추후, 소셜 로그인 사용자일 때 연동 해제 시키는 로직 추가
     *
     * @param useCase
     */
    @Transactional
    public void deleteMember(MemberIdUseCase useCase) {
        try {
            memberRepository.deleteMember(useCase.getMemberId());
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
