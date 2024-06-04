package com.keypoint.keypointtravel.entity.member;

import com.keypoint.keypointtravel.common.enumType.RoleType;
import com.keypoint.keypointtravel.common.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.entity.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OauthProviderType oauthProviderType;
}
