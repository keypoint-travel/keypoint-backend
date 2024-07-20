package com.keypoint.keypointtravel.global.utils;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtils {

    /**
     * 현재 API 요청에서 사용된 요청자의 권한을 반환하는 함수 - 요청한 API 의 권한이 없는 경우, ROLE_NONE
     *
     * @return
     */
    public static RoleType getCurrentRoleType(Authentication authentication) {
        try {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

            return RoleType.fromName(roles);
        } catch (Exception ex) {
            return RoleType.ROLE_NONE;
        }
    }
}
