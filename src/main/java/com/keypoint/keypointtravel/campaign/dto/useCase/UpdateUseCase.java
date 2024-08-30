package com.keypoint.keypointtravel.campaign.dto.useCase;

import com.keypoint.keypointtravel.campaign.dto.request.MemberInfo;
import com.keypoint.keypointtravel.campaign.dto.request.createRequest.BudgetInfo;
import com.keypoint.keypointtravel.campaign.dto.request.createRequest.EditRequest;
import com.keypoint.keypointtravel.campaign.dto.request.createRequest.TravelInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateUseCase {

    private final Long campaignId;
    private MultipartFile coverImage;
    private String title;
    private Date startDate;
    private Date endDate;
    private Long memberId;
    List<TravelInfo> travels;
    List<BudgetInfo> budgets;
    List<MemberInfo> members;

    static public UpdateUseCase of(Long campaignId, MultipartFile coverImage, EditRequest request, Long memberId) {
        return new UpdateUseCase(
            campaignId,
            coverImage,
            request.getTitle(),
            request.getStartDate(),
            request.getEndDate(),
            memberId,
            request.getTravels(),
            request.getBudgets(),
            request.getMembers()
        );
    }
}
