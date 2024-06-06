package com.keypoint.keypointtravel.config.security.session;

import com.keypoint.keypointtravel.common.enumType.RoleType;
import com.keypoint.keypointtravel.entity.member.Member;
import lombok.Getter;

@Getter
public class SessionUser {

    private String email;
    private RoleType role;

    public SessionUser(Member member) {
        this.email = member.getEmail();
        this.role = member.getRole();
    }

    public static SessionUser of(Member member) {
        return new SessionUser(member);
    }
}
