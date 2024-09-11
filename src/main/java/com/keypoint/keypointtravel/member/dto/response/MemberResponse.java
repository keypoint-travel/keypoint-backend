package com.keypoint.keypointtravel.member.dto.response;


import com.keypoint.keypointtravel.auth.dto.response.TokenInfoResponse;
import com.keypoint.keypointtravel.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String email;
    private TokenInfoResponse token;
    private String badgeUrl;

    public static MemberResponse of(Member member, TokenInfoResponse token, String badgeUrl) {
        return new MemberResponse(member.getId(), member.getEmail(), token, badgeUrl);
    }
}
