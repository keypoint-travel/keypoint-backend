package com.keypoint.keypointtravel.oauth.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class ReissueGoogleRequest {

    @JsonProperty(value = "client_id")
    private String clientId;

    @JsonProperty(value = "client_secret")
    private String clientSecret;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @JsonProperty(value = "grant_type")
    private String grantType = "refresh_token";

    @JsonProperty(value = "access_type")
    private String accessType = "offline";

    public ReissueGoogleRequest(
        String clientId,
        String clientSecret,
        String refreshToken
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.refreshToken = refreshToken;
    }

    public static ReissueGoogleRequest of(
        String clientId,
        String clientSecret,
        String refreshToken
    ) {
        return new ReissueGoogleRequest(clientId, clientSecret, refreshToken);
    }

    public MultiValueMap<String, String> toMap() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("client_id", Collections.singletonList(this.clientId));
        map.put("client_secret", Collections.singletonList(this.clientSecret));
        map.put("refresh_token", Collections.singletonList(this.refreshToken));
        map.put("grant_type", Collections.singletonList(this.grantType));
        map.put("access_type", Collections.singletonList(this.accessType));
        return map;
    }
}
