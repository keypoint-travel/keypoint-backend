package com.keypoint.keypointtravel.member.dto.dto;

import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;

public interface CommonMemberDTO {

    Long getId();

    String getName();

    String getEmail();

    String getPassword();

    RoleType getRole();

    OauthProviderType getOauthProviderType();
}
