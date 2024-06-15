package com.keypoint.keypointtravel.member.dto.response;


import com.keypoint.keypointtravel.member.entity.Member;
import lombok.Data;

@Data
public class MemberDTO {

    private Long id;
    private String email;

    public MemberDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static MemberDTO from(Member member) {
        return new MemberDTO(member.getId(), member.getEmail());
    }
}
