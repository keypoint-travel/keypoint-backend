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
    private float amount;

    public static MemberInfo from(AmountByMemberDto dto) {
        return new MemberInfo(
            dto.getMemberId(), dto.getMemberName(), dto.getMemberImage(), Math.round(dto.getAmount()));
    }

    public void addAmount(double amount) {
        this.amount += amount;
    }

    public void updateAmount(float amount) {
        this.amount = amount;
    }
}
