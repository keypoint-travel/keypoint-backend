package com.keypoint.keypointtravel.global.exception;

import java.nio.charset.Charset;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public class HttpClientException extends HttpClientErrorException {

    public HttpClientException(HttpStatusCode statusCode) {
        super(statusCode);
    }

    public HttpClientException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public HttpClientException(HttpStatusCode statusCode, String statusText, byte[] body,
        Charset responseCharset) {
        super(statusCode, statusText, body, responseCharset);
    }

    public HttpClientException(HttpStatusCode statusCode, String statusText, HttpHeaders headers,
        byte[] body, Charset responseCharset) {
        super(statusCode, statusText, headers, body, responseCharset);
    }

    public HttpClientException(String message, HttpStatusCode statusCode, String statusText,
        HttpHeaders headers, byte[] body, Charset responseCharset) {
        super(message, statusCode, statusText, headers, body, responseCharset);
    }

    public HttpClientException(HttpClientErrorException ex) {
        super(
            ex.getStatusCode(),
            ex.getLocalizedMessage()
        );
    }
}
