package com.keypoint.keypointtravel.member.dto.response;


import com.keypoint.keypointtravel.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String email;

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getEmail());
    }
}
