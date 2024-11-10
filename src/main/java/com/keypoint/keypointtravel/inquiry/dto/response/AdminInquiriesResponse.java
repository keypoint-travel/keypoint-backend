package com.keypoint.keypointtravel.inquiry.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminInquiriesResponse {

    private Long inquiryId;
    @JsonProperty(value = "isReplied")
    private boolean isReplied;
    private Long writerId;
    private String writerImage;
    private String writerName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    @JsonProperty(value = "isDeleted")
    private boolean isDeleted;

    @JsonIgnore
    public boolean isDeleted() {
        return isDeleted;
    }

    @JsonIgnore
    public boolean isReplied() {
        return isReplied;
    }
}
