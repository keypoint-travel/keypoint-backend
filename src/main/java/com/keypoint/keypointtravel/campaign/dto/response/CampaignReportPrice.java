package com.keypoint.keypointtravel.campaign.dto.response;

import com.keypoint.keypointtravel.campaign.dto.dto.date.AmountByDateDto;
import com.keypoint.keypointtravel.campaign.dto.response.member.MemberInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class CampaignReportPrice {

    private String currency;
    private float usedTotalAmount;
    private float remainingBudget;
    private List<PercentageByCategory> categoryPercentages;
    List<AmountByDateDto> dateAmounts;
    List<MemberInfo> memberAmounts;

    public CampaignReportPrice(String currency, float usedTotalAmount, float remainingBudget,
                               List<PercentageByCategory> categoryPercentages, List<AmountByDateDto> dateAmounts,
                               List<MemberInfo> memberAmounts) {
        this.currency = currency;
        this.usedTotalAmount = usedTotalAmount;
        this.remainingBudget = remainingBudget;
        this.categoryPercentages = categoryPercentages;
        this.dateAmounts = dateAmounts;
        memberAmounts.forEach(member -> member.updateAmount(Math.round(member.getAmount() * 100) / 100f));
        this.memberAmounts = memberAmounts;
    }
}
