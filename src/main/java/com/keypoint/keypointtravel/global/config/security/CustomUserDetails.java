package com.keypoint.keypointtravel.global.config.security;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.entity.Member;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {

    private Long id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public CustomUserDetails(
        Long id,
        String email,
        String password,
        Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public CustomUserDetails(
        Long id,
        String email,
        Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.email = email;
        this.authorities = authorities;
    }

    public static CustomUserDetails of(Member member, RoleType roleType) {
        List<GrantedAuthority> authorities = Collections.
            singletonList(new SimpleGrantedAuthority(roleType.name()));

        return new CustomUserDetails(
            member.getId(),
            member.getEmail(),
            member.getPassword(),
            authorities
        );
    }

    public static CustomUserDetails of(CommonMemberDTO member) {
        List<GrantedAuthority> authorities = Collections.
            singletonList(new SimpleGrantedAuthority(member.getRole().name()));

        return new CustomUserDetails(
            member.getId(),
            member.getEmail(),
            member.getPassword(),
            authorities
        );
    }

    public static CustomUserDetails of(
        CommonMemberDTO member,
        Map<String, Object> attributes
    ) {
        CustomUserDetails userDetails = CustomUserDetails.of(member);
        userDetails.setAttributes(attributes);
        return userDetails;
    }

    public static CustomUserDetails of(
        Long id,
        String email,
        Collection<? extends GrantedAuthority> authorities
    ) {
        return new CustomUserDetails(id, email, authorities);
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public String getUsername() {
        return this.getName();
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
