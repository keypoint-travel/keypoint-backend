package com.keypoint.keypointtravel.campaign.dto.useCase;

import com.keypoint.keypointtravel.campaign.dto.request.createRequest.BudgetInfo;
import com.keypoint.keypointtravel.campaign.dto.request.createRequest.CreateRequest;
import com.keypoint.keypointtravel.campaign.dto.request.MemberInfo;
import com.keypoint.keypointtravel.campaign.dto.request.createRequest.TravelInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreateUseCase {

    private MultipartFile coverImage;
    private String title;
    private Date startDate;
    private Date endDate;
    private Long memberId;
    List<TravelInfo> travels;
    List<BudgetInfo> budgets;
    List<MemberInfo> members;

    static public CreateUseCase of(
        MultipartFile coverImage, CreateRequest request, Long memberId) {
        return new CreateUseCase(
            coverImage,
            request.getTitle(),
            request.getStartDate(),
            request.getEndDate(),
            memberId,
            request.getTravels(),
            request.getBudgets(),
            request.getFriends()
        );
    }
}
