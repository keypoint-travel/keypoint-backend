package com.keypoint.keypointtravel.friend;

import com.keypoint.keypointtravel.friend.repository.FriendRepository;
import com.keypoint.keypointtravel.global.enumType.member.GenderType;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.entity.MemberDetail;
import com.keypoint.keypointtravel.member.repository.MemberDetailRepository;
import com.keypoint.keypointtravel.member.repository.MemberRepository;
import config.QueryDslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(QueryDslConfig.class)
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
        MemberDetail detail = new MemberDetail(member, GenderType.MAN, LocalDate.now(), "test", LanguageCode.KO, "KR");
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
        assertThatThrownBy(() -> friendRepository.findMemberByEmailOrInvitationCode("block@dump.com"))
            .isInstanceOf(GeneralException.class);
    }
}
