package com.keypoint.keypointtravel.campaign.dto.response.member;

import com.keypoint.keypointtravel.campaign.dto.response.PercentageByCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PercentageByMemberResponse {

    private String currency;
    private List<PercentageByCategory> percentages;
}
