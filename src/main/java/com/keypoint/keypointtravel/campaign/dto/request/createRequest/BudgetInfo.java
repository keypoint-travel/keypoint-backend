package com.keypoint.keypointtravel.campaign.dto.request.createRequest;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BudgetInfo {

    @NotEmpty(message = "카테고리를 입력해주세요.")
    @NotBlank(message = "카테고리를 입력해주세요.")
    @Size(max = 20, message = "카테고리의 최대길이를 초과하였습니다.")
    private String category;

    @NotEmpty(message = "금액을 입력해 주세요")
    @Min(value = 1, message = "금액은 1원 이상이어야 합니다.")
    private float amount;

    private CurrencyType currency;
}
