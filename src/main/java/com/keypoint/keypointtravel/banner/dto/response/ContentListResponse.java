package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.Body;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ContentListResponse {

    //content
    private List<BannerDetails> contents;

    //page 정보
    private boolean last;
    private int totalPages;
    private int totalElements;
    private boolean first;
    private int size;
    private int numberOfElements;
    private boolean empty;
    private int pageNumber;

    public static ContentListResponse from(Body data) {
        return ContentListResponse.builder()
            .contents(data.getItems().getItem().stream().map(BannerDetails::from).toList())
            .last(data.getPageNo() == getTotalPage(data.getTotalCount()))
            .totalPages(getTotalPage(data.getTotalCount()))
            .totalElements(data.getTotalCount())
            .first(data.getPageNo() == 1)
            .size(10)
            .numberOfElements(data.getNumOfRows())
            .empty(data.getNumOfRows() == 0)
            .pageNumber(data.getPageNo())
            .build();
    }

    private static int getTotalPage(int totalCount) {
        return totalCount / 10 + ((totalCount % 10 > 0) ? 1 : 0);
    }
}