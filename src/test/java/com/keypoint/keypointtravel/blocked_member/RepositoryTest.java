package com.keypoint.keypointtravel.blocked_member;

import com.keypoint.keypointtravel.blocked_member.dto.BlockedMemberDto;
import com.keypoint.keypointtravel.blocked_member.entity.BlockedMember;
import com.keypoint.keypointtravel.blocked_member.repository.BlockedMemberRepository;
import com.keypoint.keypointtravel.global.enumType.member.GenderType;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.entity.MemberDetail;
import config.QueryDslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
@ActiveProfiles("test")
public class RepositoryTest {

    @Autowired
    private BlockedMemberRepository blockedMemberRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    public void existsByBlockedMemberIdAndMemberIdTest() {
        // given
        Member member1 = new Member("email1@test.com", OauthProviderType.GOOGLE);
        Member member2 = new Member("email2@test.com", OauthProviderType.GOOGLE);
        em.persist(member1);
        em.persist(member2);
        // member2가 member1을 차단하는 객체
        BlockedMember blockedMember = new BlockedMember(member1.getId(), member2);
        em.persist(blockedMember);
        em.flush();
        em.clear();

        // when
        // member1이 member2를 차단했는지 확인
        boolean result1 = blockedMemberRepository.existsByBlockedMemberIdAndMemberId(member2.getId(), member1.getId());
        // member2가 member1을 차단했는지 확인
        boolean result2 = blockedMemberRepository.existsByBlockedMemberIdAndMemberId(member1.getId(), member2.getId());

        // then
        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
    }

    @Test
    public void existsBlockedMemberTest() {
        // given
        Member member1 = new Member("email1@test.com", OauthProviderType.GOOGLE);
        Member member2 = new Member("email2@test.com", OauthProviderType.GOOGLE);
        Member member3 = new Member("email3@test.com", OauthProviderType.GOOGLE);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        // member2가 member1을 차단하는 객체
        BlockedMember blockedMember1 = new BlockedMember(member1.getId(), member2);
        // member1이 member3을 차단하는 객체
        BlockedMember blockedMember2 = new BlockedMember(member3.getId(), member1);
        em.persist(blockedMember1);
        em.persist(blockedMember2);
        em.flush();
        em.clear();

        //when
        // member1과 member2중 둘 중 한쪽이 차단했는지 확인
        boolean result1 = blockedMemberRepository.existsBlockedMember(member1.getId(), member2.getId());
        // member1과 member3중 둘 중 한쪽이 차단했는지 확인
        boolean result2 = blockedMemberRepository.existsBlockedMember(member1.getId(), member3.getId());
        // member2과 member3중 둘 중 한쪽이 차단했는지 확인
        boolean result3 = blockedMemberRepository.existsBlockedMember(member2.getId(), member3.getId());

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
    }

    @Test
    public void findBlockedMembersTest() {
        // given
        Member member1 = new Member("email1@test.com", OauthProviderType.GOOGLE);
        Member member2 = new Member("email2@test.com", OauthProviderType.GOOGLE);
        Member member3 = new Member("email3@test.com", OauthProviderType.GOOGLE);
        MemberDetail detail2 = new MemberDetail(member2, GenderType.MAN, LocalDate.now(), "test2",
            LanguageCode.KO, "KR");
        member2.setMemberDetail(detail2);
        MemberDetail detail3 = new MemberDetail(member3, GenderType.MAN, LocalDate.now(), "test3",
            LanguageCode.KO, "KR");
        member3.setMemberDetail(detail3);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(detail2);
        em.persist(detail3);
        // member1이 member2을 차단하는 객체
        BlockedMember blockedMember1 = new BlockedMember(member2.getId(), member1);
        // member1이 member3을 차단하는 객체
        BlockedMember blockedMember2 = new BlockedMember(member3.getId(), member1);
        em.persist(blockedMember1);
        em.persist(blockedMember2);

        // when member1이 차단한 회원들을 조회
        List<BlockedMemberDto> dtoList = blockedMemberRepository.findBlockedMembers(member1.getId());

        //then
        assertThat(dtoList.size()).isEqualTo(2);
        assertThat(dtoList.get(0).getMemberId()).isEqualTo(member3.getId());
        assertThat(dtoList.get(0).getMemberName()).isEqualTo(member3.getMemberDetail().getName());
    }
}
