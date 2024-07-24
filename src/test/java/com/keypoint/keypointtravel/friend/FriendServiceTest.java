package com.keypoint.keypointtravel.friend;

import com.keypoint.keypointtravel.friend.dto.SaveUseCase;
import com.keypoint.keypointtravel.friend.entity.Friend;
import com.keypoint.keypointtravel.friend.repository.FriendRepository;
import com.keypoint.keypointtravel.friend.service.FriendService;
import com.keypoint.keypointtravel.global.enumType.member.GenderType;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.entity.MemberDetail;
import com.keypoint.keypointtravel.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private MemberRepository memberRepository;

    @Captor
    private ArgumentCaptor<Friend> friendCaptor;

    @InjectMocks
    private FriendService friendService;

    @Test
    public void saveFriendTest() {
        // given
        SaveUseCase saveUseCase = new SaveUseCase("test@test.com", 1L);
        Member member = new Member("email@test.com", OauthProviderType.GOOGLE);
        Member friendMember = new Member("friend@test.com", OauthProviderType.GOOGLE);
        MemberDetail detail = new MemberDetail(member, GenderType.MAN, LocalDate.now(), "test", LanguageCode.KO, "KR");
        friendMember.setMemberDetail(detail);

        when(friendRepository.findMemberByEmailOrInvitationCode(anyString())).thenReturn(friendMember);
        when(memberRepository.getReferenceById(anyLong())).thenReturn(member);

        // when
        friendService.saveFriend(saveUseCase);

        // then
        verify(friendRepository).save(friendCaptor.capture());
        Friend savedFriend = friendCaptor.getValue();
        assertThat(friendMember.getMemberDetail().getName()).isEqualTo(savedFriend.getFriendName());
    }
}
