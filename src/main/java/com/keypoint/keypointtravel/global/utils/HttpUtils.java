package com.keypoint.keypointtravel.global.utils;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.interceptor.HttpLoggingInterceptor;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class HttpUtils {

    private static RestTemplate restTemplate;

    static {
        restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors.add(new HttpLoggingInterceptor());
    }

    public static <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> className) {
        try {
            HttpEntity entity = new HttpEntity(headers);
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            return restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                className
            );
        } catch (HttpClientErrorException ex) {
            throw ex;
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
            HttpEntity<Object> entity = new HttpEntity<Object>(body, headers);

            return restTemplate.postForEntity(url, entity, className);
        } catch (HttpClientErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new GeneralException(CommonErrorCode.OPEN_API_REQUEST_FAIL, ex.getMessage());
        }
    }
}
