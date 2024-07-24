package com.keypoint.keypointtravel.friend.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "friend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long friendId;

    @Column(nullable = false)
    private String friendName;

    @Column(nullable = false)
    private String profileImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private boolean isDeleted;

    public Friend(Long friendId, String friendName, String profileImageId, Member member, boolean isDeleted) {
        this.friendId = friendId;
        this.friendName = friendName;
        this.profileImageId = profileImageId;
        this.member = member;
        this.isDeleted = isDeleted;
    }
}
