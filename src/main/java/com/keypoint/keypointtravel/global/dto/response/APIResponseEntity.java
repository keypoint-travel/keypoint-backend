package com.keypoint.keypointtravel.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APIResponseEntity<T> {

    /**
     * Explain data
     */
    private String message;

    private T data;

    public static <T> APIResponseEntity<PageResponse<T>> toPage(
        String message,
        Page<T> page
    ) {
        PageResponse<T> pageResponse = PageResponse.<T>builder()
            .content(page.getContent())
            .total(page.getTotalElements())
            .build();

        return APIResponseEntity.<PageResponse<T>>builder()
            .message(message)
            .data(pageResponse)
            .build();
    }

    public static <T> APIResponseEntity<SliceResponse<T>> toSlice(
        String message,
        Slice<T> slice
    ) {
        SliceResponse<T> sliceResponse = SliceResponse.<T>builder()
            .content(slice.getContent())
            .isLast(slice.isLast())
            .build();

        return APIResponseEntity.<SliceResponse<T>>builder()
            .message(message)
            .data(sliceResponse)
            .build();
    }
}

