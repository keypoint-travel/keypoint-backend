package com.keypoint.keypointtravel.inquiry.dto.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keypoint.keypointtravel.inquiry.dto.response.ImageUrlResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserInquiryDto {

        private LocalDateTime createdAt;
        private String content;
        @JsonProperty(value = "isAdmin")
        private boolean isAdmin;
        private List<ImageUrlResponse> images;

        @JsonIgnore
        public boolean isAdmin() {
                return isAdmin;
        }
}
