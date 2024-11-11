package com.keypoint.keypointtravel.inquiry.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class InquiriesUseCase {

    private String sortBy;
    private String direction;
    private Pageable pageable;
    private String content;

    public static InquiriesUseCase of(String sortBy, String direction, Pageable pageable, String content) {
        if(content != null && content.isEmpty()){
            content = null;
        }
        return new InquiriesUseCase(sortBy, direction, pageable, content);
    }
}
