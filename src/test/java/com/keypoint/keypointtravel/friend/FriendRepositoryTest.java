package com.keypoint.keypointtravel.friend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.keypoint.keypointtravel.friend.entity.Friend;
import com.keypoint.keypointtravel.friend.repository.FriendRepository;
import com.keypoint.keypointtravel.global.enumType.member.GenderType;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.entity.MemberDetail;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import config.QueryDslConfig;
import java.time.LocalDate;
import java.util.List;

import config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(TestConfig.class)
@ActiveProfiles("test")
public class FriendRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberDetailRepository memberDetailRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    public void findMemberByEmailOrInvitationCodeTest() {
        //given
        String email = "test@test.com";
        String invitationCode = "testesttest";
        Member member = new Member(email, OauthProviderType.GOOGLE);
        member.setInvitationCode(invitationCode);
        memberRepository.save(member);
        MemberDetail detail = new MemberDetail(member, GenderType.MAN, LocalDate.now(), "test",
            LanguageCode.KO, "KR");
        memberDetailRepository.save(detail);

        em.flush();
        em.clear();

        //when
        Member findMember1 = friendRepository.findMemberByEmailOrInvitationCode(email);
        Member findMember2 = friendRepository.findMemberByEmailOrInvitationCode(invitationCode);

        //then
        assertThat(findMember1.getId()).isEqualTo(findMember2.getId());

        //when & then : 이메일, 초대코드에 해당하지 않을 시 예외처리
        assertThatThrownBy(() -> friendRepository.findMemberByEmailOrInvitationCode("block"))
            .isInstanceOf(GeneralException.class);
        assertThatThrownBy(
            () -> friendRepository.findMemberByEmailOrInvitationCode("block@dump.com"))
            .isInstanceOf(GeneralException.class);
    }

    @Test
    public void updateIsDeletedByIdTest() {
        //given : 친구 관계 생성
        Member member1 = new Member("test@gmail.com", OauthProviderType.GOOGLE);
        Member member2 = new Member("test@naver.com", OauthProviderType.GOOGLE);
        member1.setInvitationCode("invitationCode1");
        member1.setInvitationCode("invitationCode2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        friendRepository.save(new Friend(member1.getId(), member2, false));
        friendRepository.save(new Friend(member2.getId(), member1, false));
        em.flush();
        em.clear();

        //when : 친구 관계 삭제
        friendRepository.updateIsDeletedById(member1.getId(), member2.getId(), true);

        //then : 친구 관계 삭제 확인
        List<Friend> friendList = friendRepository.findAll();
        for (Friend friend : friendList) {
            assertThat(friend.isDeleted()).isTrue();
        }
    }
}
