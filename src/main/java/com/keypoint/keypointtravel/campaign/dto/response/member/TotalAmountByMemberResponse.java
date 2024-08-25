package com.keypoint.keypointtravel.campaign.dto.response.member;

import com.keypoint.keypointtravel.campaign.dto.dto.member.AmountByMemberDto;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class TotalAmountByMemberResponse {

    private CurrencyType currency;
    private List<MemberInfo> members;

    public static TotalAmountByMemberResponse of(CurrencyType currency, List<AmountByMemberDto> dtoList) {
        List<MemberInfo> members = new ArrayList<>();
        for (int i = 0; i < dtoList.size(); i++) {
            if(i == 0){
                members.add(MemberInfo.from(dtoList.get(i)));
            } else {
                if(Objects.equals(dtoList.get(i - 1).getMemberId(), dtoList.get(i).getMemberId())){
                    members.get(members.size() - 1).updateAmount(dtoList.get(i).getAmount());
                } else {
                    members.add(MemberInfo.from(dtoList.get(i)));
                }
            }
        }
        return new TotalAmountByMemberResponse(currency, members);
    }
}
