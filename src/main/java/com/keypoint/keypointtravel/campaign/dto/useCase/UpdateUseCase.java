package com.keypoint.keypointtravel.campaign.dto.useCase;

import com.keypoint.keypointtravel.campaign.dto.request.MemberInfo;
import com.keypoint.keypointtravel.campaign.dto.request.createRequest.BudgetInfo;
import com.keypoint.keypointtravel.campaign.dto.request.createRequest.EditRequest;
import com.keypoint.keypointtravel.campaign.dto.request.createRequest.TravelInfo;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.MultiPartFileUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

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
        try {
            if (coverImage == null || coverImage.isEmpty() || coverImage.getContentType() == null) {
                coverImage = MultiPartFileUtils.getImageAsMultipartFile("campaign_default.jpg");
            }
        } catch (Exception e) {
            throw new GeneralException(e);
        }
        if (!Objects.requireNonNull(coverImage.getContentType()).startsWith("image/")) {
            throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA);
        }
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
