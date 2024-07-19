package com.keypoint.keypointtravel.member.service;

import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.dto.useCase.EmailUseCase;
import com.keypoint.keypointtravel.member.repository.MemberRepository;
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
}
