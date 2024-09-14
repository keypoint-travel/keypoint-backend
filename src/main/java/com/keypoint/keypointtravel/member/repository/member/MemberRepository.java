package com.keypoint.keypointtravel.member.repository.member;

import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Optional<CommonMemberDTO> findByEmailAndIsDeletedFalse(String email);

    boolean existsByEmailAndIsDeletedFalse(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.recentLoginAt = :recentLoginAt WHERE m.id = :id")
    int updateRecentLoginAtByMemberId(
        @Param("id") Long id,
        @Param("recentLoginAt") LocalDateTime recentLoginAt
    );

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.role = :role WHERE m.id = :id")
    int updateRole(
        @Param("id") Long id,
        @Param("role") RoleType role
    );

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.password = :password WHERE m.id = :id")
    int updatePassword(
        @Param("id") Long id,
        @Param("password") String password
    );

    @Query("SELECT m.invitationCode FROM Member m WHERE m.id = :id")
    String findInvitationCodeByMemberId(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.isDeleted = true WHERE m.id = :id")
    int deleteMember(
            @Param("id") Long id
    );

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.oauthProviderType = :oauthProviderType WHERE m.id = :id")
    int updateOauthProviderTypeByMemberId(
        @Param("id") Long id,
        @Param("oauthProviderType") OauthProviderType oauthProviderType
    );
}
