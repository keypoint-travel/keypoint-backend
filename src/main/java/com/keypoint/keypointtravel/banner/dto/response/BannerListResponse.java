package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.Body;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BannerListResponse {

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

    public static BannerListResponse from(Body data) {
        return BannerListResponse.builder()
            .contents(data.getItems().getItem().stream().map(BannerDetails::from).toList())
            .last(data.getPageNo() == data.getTotalCount() / 10 + 1)
            .totalPages(data.getTotalCount() / 10 + 1)
            .totalElements(data.getTotalCount())
            .first(data.getPageNo() == 1)
            .size(10)
            .numberOfElements(data.getNumOfRows())
            .empty(data.getNumOfRows() == 0)
            .pageNumber(data.getPageNo())
            .build();

    }
}