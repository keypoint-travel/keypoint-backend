package com.keypoint.keypointtravel.campaign.dto.request.createRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class CreateRequest {

    @NotEmpty(message = "제목을 입력해주세요.")
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 20, message = "제목의 최대길이를 초과하였습니다.")
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    List<TravelInfo> travels;
    List<BudgetInfo> budgets;
    List<MemberInfo> members;

    public CreateRequest(String title, Date startDate, Date endDate, List<TravelInfo> travels, List<BudgetInfo> budgets, List<MemberInfo> members) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.travels = Optional.ofNullable(travels).orElseGet(ArrayList::new);
        this.budgets = Optional.ofNullable(budgets).orElseGet(ArrayList::new);
        this.members = Optional.ofNullable(members).orElseGet(ArrayList::new);
    }
}
