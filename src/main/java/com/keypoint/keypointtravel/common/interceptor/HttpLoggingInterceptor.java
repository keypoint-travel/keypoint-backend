package com.keypoint.keypointtravel.common.interceptor;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class HttpLoggingInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request,
      byte[] body,
      ClientHttpRequestExecution execution
  ) throws IOException {
    logRequest(request, body);
    ClientHttpResponse response = execution.execute(request, body);
    logResponse(response);
    return response;
  }

  private void logRequest(HttpRequest request, byte[] body) throws IOException {
    System.out.println("URI         : " + request.getURI());
    System.out.println("Method      : " + request.getMethod());
  }

  private void logResponse(ClientHttpResponse response) throws IOException {
    System.out.println("Status code  : " + response.getStatusCode());
    System.out.println("Status text  : " + response.getStatusText());
  }
}
