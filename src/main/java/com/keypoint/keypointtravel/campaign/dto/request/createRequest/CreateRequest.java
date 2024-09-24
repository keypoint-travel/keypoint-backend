package com.keypoint.keypointtravel.campaign.dto.request.createRequest;

import com.keypoint.keypointtravel.campaign.dto.request.MemberInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
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
    private float totalBudget;
    List<BudgetInfo> budgets;
    List<MemberInfo> friends;
    List<EmailInfo> emails;

    public CreateRequest(String title, Date startDate, Date endDate, List<TravelInfo> travels,
        float totalBudget, List<BudgetInfo> budgets, List<MemberInfo> friends,
        List<EmailInfo> emails) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.travels = Optional.ofNullable(travels).orElseGet(ArrayList::new);
        this.totalBudget = totalBudget;
        this.budgets = Optional.ofNullable(budgets).orElseGet(ArrayList::new);
        this.friends = Optional.ofNullable(friends).orElseGet(ArrayList::new);
        this.emails = Optional.ofNullable(emails).orElseGet(ArrayList::new);
    }
}
