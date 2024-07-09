package com.keypoint.keypointtravel.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}

