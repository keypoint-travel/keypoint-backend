package com.keypoint.keypointtravel.global.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private long total;
}
