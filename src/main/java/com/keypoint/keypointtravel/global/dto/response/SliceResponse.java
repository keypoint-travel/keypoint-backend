package com.keypoint.keypointtravel.global.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SliceResponse<T> {

    private List<T> content;

    @JsonProperty("isLast")
    private boolean isLast;
}
