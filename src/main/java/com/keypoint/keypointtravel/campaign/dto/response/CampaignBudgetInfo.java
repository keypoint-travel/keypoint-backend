package com.keypoint.keypointtravel.campaign.dto.response;

import com.keypoint.keypointtravel.campaign.entity.CampaignBudget;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CampaignBudgetInfo {

    private CurrencyType currency;
    private String category;
    private float amount;

    public static CampaignBudgetInfo from(CampaignBudget campaignBudget){
        return new CampaignBudgetInfo(
            campaignBudget.getCurrency(),
            campaignBudget.getCategory(),
            campaignBudget.getAmount());
    }
}
