package com.keypoint.keypointtravel.member.dto.response;


import com.keypoint.keypointtravel.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDTO {

    private Long id;
    private String email;

    public static MemberDTO from(Member member) {
        return new MemberDTO(member.getId(), member.getEmail());
    }
}
