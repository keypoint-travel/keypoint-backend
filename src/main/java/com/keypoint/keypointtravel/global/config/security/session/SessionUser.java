package com.keypoint.keypointtravel.global.config.security.session;


import com.keypoint.keypointtravel.global.enumType.RoleType;
import com.keypoint.keypointtravel.member.entity.Member;
import lombok.Getter;

@Getter
public class SessionUser {

    private String email;
    private RoleType role;

    public SessionUser(Member member) {
        this.email = member.getEmail();
        this.role = member.getRole();
    }

    public static SessionUser from(Member member) {
        return new SessionUser(member);
    }
}
