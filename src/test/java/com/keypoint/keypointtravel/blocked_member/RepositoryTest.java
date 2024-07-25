package com.keypoint.keypointtravel.blocked_member;

import com.keypoint.keypointtravel.blocked_member.entity.BlockedMember;
import com.keypoint.keypointtravel.blocked_member.repository.BlockedMemberRepository;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.member.entity.Member;
import config.QueryDslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

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
}
