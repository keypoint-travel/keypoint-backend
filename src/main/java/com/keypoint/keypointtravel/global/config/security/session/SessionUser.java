package com.keypoint.keypointtravel.global.config.security.session;


import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import lombok.Getter;

@Getter
public class SessionUser {

    private String email;
    private RoleType role;

    public SessionUser(CommonMemberDTO member) {
        this.email = member.getEmail();
        this.role = member.getRole();
    }

    public static SessionUser from(CommonMemberDTO member) {
        return new SessionUser(member);
    }
}
