package com.keypoint.keypointtravel.dto.member.response;

import com.keypoint.keypointtravel.entity.member.Member;
import lombok.Data;

@Data
public class MemberDTO {

    private Long id;
    private String email;

    public MemberDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static MemberDTO toDTO(Member member) {
        return new MemberDTO(member.getId(), member.getEmail());
    }
}
