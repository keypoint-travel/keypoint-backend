package com.keypoint.keypointtravel.inquiry.dto.response;

import com.keypoint.keypointtravel.inquiry.dto.dto.UserInquiryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserInquiryResponse {

    private Long inquiryId;
    private List<UserInquiryDto> messages;
}
