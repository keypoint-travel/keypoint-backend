package com.keypoint.keypointtravel.inquiry.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserInquiriesResponse {

    private Long inquiryId;
    @JsonProperty(value = "isReplied")
    private boolean isReplied;
    private String content;
    private LocalDateTime createdAt;

    @JsonIgnore
    public boolean isReplied() {
        return isReplied;
    }
}
