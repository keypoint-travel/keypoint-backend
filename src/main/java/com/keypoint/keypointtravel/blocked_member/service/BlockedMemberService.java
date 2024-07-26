package com.keypoint.keypointtravel.blocked_member.service;

import com.keypoint.keypointtravel.blocked_member.dto.BlockedMemberUseCase;
import com.keypoint.keypointtravel.blocked_member.entity.BlockedMember;
import com.keypoint.keypointtravel.blocked_member.repository.BlockedMemberRepository;
import com.keypoint.keypointtravel.friend.repository.FriendRepository;
import com.keypoint.keypointtravel.global.enumType.error.BlockedMemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlockedMemberService {

    private final BlockedMemberRepository blockedMemberRepository;

    private final MemberRepository memberRepository;

    private final FriendRepository friendRepository;

    /**
     * 회원 차단 함수
     *
     * @Param 회원의 memberId, 차단할 회원의 memberId
     */
    @Transactional
    public void blockMember(BlockedMemberUseCase useCase) {
        // 1. 이미 차단되어있는지 확인
        if (blockedMemberRepository.existsByBlockedMemberIdAndMemberId(useCase.getBlockedMemberId(), useCase.getMyId())) {
            throw new GeneralException(BlockedMemberErrorCode.ALREADY_BLOCKED);
        }
        // 2. 상대 회원이 존재하는지 확인
        if (!memberRepository.existsById(useCase.getBlockedMemberId())) {
            throw new GeneralException(MemberErrorCode.NOT_EXISTED_MEMBER);
        }
        // 3. 차단하기
        Member member = memberRepository.getReferenceById(useCase.getMyId());
        blockedMemberRepository.save(new BlockedMember(useCase.getBlockedMemberId(), member));
        // 4. 차단된 회원과 친구 관계인 경우 친구 관계 삭제
        friendRepository.updateIsDeletedById(useCase.getBlockedMemberId(), useCase.getMyId());
    }

    /**
     * 회원 차단 해제 함수
     *
     * @Param 회원의 memberId, 차단할 회원의 memberId
     */
    @Transactional
    public void unblockMember(BlockedMemberUseCase useCase) {
        // 1. 차단 해제
        long count = blockedMemberRepository.deleteByBlockedMemberIdAndMemberId(
            useCase.getBlockedMemberId(), useCase.getMyId());
        // 2. 차단된 기록이 없었을 경우
        if(count < 1){
            throw new GeneralException(BlockedMemberErrorCode.NOT_EXISTED_BLOCKED);
        }
    }
}
