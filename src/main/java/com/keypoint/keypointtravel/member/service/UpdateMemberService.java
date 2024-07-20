package com.keypoint.keypointtravel.member.service;

import com.keypoint.keypointtravel.member.repository.MemberRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UpdateMemberService {

    private final MemberRepository memberRepository;

    /**
     * 최근 로그인 날짜 정보를 업데이트하는 함수
     *
     * @param memberId 최근 로그인 날짜 정보를 업데이트 사용자 아이디
     */
    @Transactional
    public void updateRecentLoginAtByMemberId(Long memberId) {
        memberRepository.updateRecentLoginAtByMemberId(memberId, LocalDateTime.now());
    }
}
