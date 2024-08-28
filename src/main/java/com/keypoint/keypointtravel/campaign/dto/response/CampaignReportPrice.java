package com.keypoint.keypointtravel.campaign.dto.response;

import com.keypoint.keypointtravel.campaign.dto.dto.date.AmountByDateDto;
import com.keypoint.keypointtravel.campaign.dto.dto.member.AmountByMemberDto;
import com.keypoint.keypointtravel.campaign.dto.response.member.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class CampaignReportPrice {

    private String currency;
    private float usedTotalAmount;
    private float remainingBudget;
    private List<PercentageByCategory> categoryPercentages;
    List<AmountByDateDto> dateAmounts;
    List<MemberInfo> memberAmounts;

    public static CampaignReportPrice of(String currency, float usedTotalAmount, float remainingBudget,
                               List<PercentageByCategory> percentages, List<AmountByDateDto> amountsByDate,
                               List<AmountByMemberDto> amountsByMember) {
        List<MemberInfo> members = new ArrayList<>();
        for (int i = 0; i < amountsByMember.size(); i++) {
            if(i == 0){
                members.add(MemberInfo.from(amountsByMember.get(i)));
            } else {
                if(Objects.equals(amountsByMember.get(i - 1).getMemberId(), amountsByMember.get(i).getMemberId())){
                    members.get(members.size() - 1).updateAmount(amountsByMember.get(i).getAmount());
                } else {
                    members.add(MemberInfo.from(amountsByMember.get(i)));
                }
            }
        }
        return CampaignReportPrice.builder()
            .currency(currency)
            .usedTotalAmount(usedTotalAmount)
            .remainingBudget(remainingBudget)
            .categoryPercentages(percentages)
            .dateAmounts(amountsByDate)
            .memberAmounts(members)
            .build();
    }
}
