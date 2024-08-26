package com.keypoint.keypointtravel.campaign.dto.response.member;

import com.keypoint.keypointtravel.campaign.dto.dto.member.AmountByMemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfo {

    private Long memberId;
    private String memberName;
    private String memberImage;
    private Long amount;

    public static MemberInfo from(AmountByMemberDto dto) {
        return new MemberInfo(
            dto.getMemberId(), dto.getMemberName(), dto.getMemberImage(), Math.round(dto.getAmount()));
    }

    public void updateAmount(double amount) {
        this.amount += Math.round(amount);
    }
}
