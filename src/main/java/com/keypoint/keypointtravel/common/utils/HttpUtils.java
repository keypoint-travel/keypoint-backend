package com.keypoint.keypointtravel.common.utils;

import com.keypoint.keypointtravel.common.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.common.exception.GeneralException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class HttpUtils {

    public static <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> className) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpEntity entity = new HttpEntity(headers);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            ResponseEntity<T> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                className
            );

            return response;
        } catch (Exception ex) {
            throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL, ex.getMessage());
        }
    }

    public static <T> ResponseEntity<T> post(
        String url,
        HttpHeaders headers,
        Object body,
        Class<T> className
    ) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpEntity<Object> entity = new HttpEntity<Object>(body, headers);

            ResponseEntity<T> response = restTemplate.postForEntity(url, entity, className);

            return response;
        } catch (Exception ex) {
            throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL, ex.getMessage());
        }
    }
}
