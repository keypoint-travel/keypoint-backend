package com.keypoint.keypointtravel.global.interceptor;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HttpLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
        HttpRequest request,
        byte[] body,
        ClientHttpRequestExecution execution
    ) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);
        addLog(request, response);
        return response;
    }

    private void addLog(HttpRequest request, ClientHttpResponse response) throws IOException {
        log.info(String.format("uri={%s} http-method={%s} status code={%s}",
            request.getURI(),
            request.getMethod(),
            response.getStatusCode()
        ));
    }
}
