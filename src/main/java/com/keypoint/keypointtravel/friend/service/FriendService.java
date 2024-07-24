package com.keypoint.keypointtravel.friend.service;

import com.keypoint.keypointtravel.friend.dto.FriendsResponse;
import com.keypoint.keypointtravel.friend.dto.SaveUseCase;
import com.keypoint.keypointtravel.friend.entity.Friend;
import com.keypoint.keypointtravel.friend.repository.FriendRepository;
import com.keypoint.keypointtravel.global.enumType.error.FriendErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    private final MemberRepository memberRepository;

    /**
     * 친구 생성 함수
     *
     * @Param 회원의 memberId, 친구 코드 or 이메일
     */
    @Transactional
    public void saveFriend(SaveUseCase saveUseCase) {
        // 1. 친구 코드에 해당하는 회원 조회 : 없을 시 예외 처리 fetch join
        Member findedMember = friendRepository.findMemberByEmailOrInvitationCode(saveUseCase.getInvitationValue());
        validate(findedMember, saveUseCase.getMemberId());
        // 2. 회원 정보 조회
        Member member = memberRepository.findById(saveUseCase.getMemberId()).orElseThrow(
            () -> new GeneralException(MemberErrorCode.NOT_EXISTED_MEMBER));
        // 3. 친구 생성 및 저장(서로 생성하기에 두번 시행)
        friendRepository.save(buildFriend(findedMember, member));
        friendRepository.save(buildFriend(member, findedMember));
    }

    private Friend buildFriend(Member findedMember, Member member){
        return Friend.builder()
            .friendId(findedMember.getId())
            .friendName(findedMember.getMemberDetail().getName())
            .profileImageId(findedMember.getMemberDetail().getProfileImageId())
            .member(member)
            .isDeleted(false)
            .build();
    }

    private void validate(Member findedMember, Long myId){
        if(findedMember.getId().equals(myId)){
            throw new GeneralException(FriendErrorCode.CANNOT_ADD_SELF);
        }
        if(friendRepository.existsByFriendIdAndMemberIdAndIsDeletedFalse(findedMember.getId(), myId)) {
            throw new GeneralException(FriendErrorCode.DUPLICATED_FRIEND);
        }
    }

    /**
     * 친구 목록 조회 함수
     *
     * @Param 회원의 memberId
     *
     * @Return 조회한 친구 목록 Dto(회원의 초대 코드, 친구 리스트(친구 아이디, 친구 이름, 친구 프로필 이미지 아이디))
     */
    @Transactional(readOnly = true)
    public FriendsResponse findFriendList(Long memberId) {
        String invitationCode = memberRepository.findInvitationCodeByMemberId(memberId);
        List<Friend> friends = friendRepository.findAllByMemberId(memberId);
        return FriendsResponse.of(invitationCode, friends);
    }

    /**
     * 친구 삭제 함수(내 기준, 친구 기준 두 가지 siDeleted를 true로 변경)
     *
     * @Param 회원의 memberId, 친구의 friendId
     */
    @Transactional
    public void deleteFriend(Long memberId, Long friendId) {
        long count = friendRepository.updateIsDeletedById(memberId, friendId);
        if (count < 2) {
            throw new GeneralException(FriendErrorCode.NOT_EXISTED_FRIEND);
        }
    }
}