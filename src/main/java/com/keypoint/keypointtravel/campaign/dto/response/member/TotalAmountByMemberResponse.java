package com.keypoint.keypointtravel.campaign.dto.response.member;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class TotalAmountByMemberResponse {

    private CurrencyType currency;
    private List<MemberInfo> members;

    public TotalAmountByMemberResponse(CurrencyType currency, List<MemberInfo> members) {
        this.currency = currency;
        members.forEach(member -> member.updateAmount(Math.round(member.getAmount() * 100) / 100f));
        this.members = members;
    }
}
