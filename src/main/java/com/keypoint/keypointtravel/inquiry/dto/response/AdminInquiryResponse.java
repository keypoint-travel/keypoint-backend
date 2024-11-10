package com.keypoint.keypointtravel.inquiry.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keypoint.keypointtravel.inquiry.dto.dto.UserInquiryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminInquiryResponse {

    private Long inquiryId;
    @JsonProperty(value = "isReplied")
    private boolean isReplied;
    private List<UserInquiryDto> messages;

    @JsonIgnore
    public boolean isReplied() {
        return isReplied;
    }
}
