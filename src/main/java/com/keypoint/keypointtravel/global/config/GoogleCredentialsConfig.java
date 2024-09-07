package com.keypoint.keypointtravel.global.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

@Component
public class GoogleCredentialsConfig {

    // todo: 안드로이드 앱 패키지 이름 - 앱 출시 후 패키지 이름으로 수정
    private String packageName = "com.keypoint.keypointtravel.premium";

    // Google API를 사용하기 위한 인증 정보를 저장하는 객체
    private GoogleCredentials credentials;

    // Google Play Developer API를 사용하기 위한 객체
    public AndroidPublisher androidPublisher () throws IOException, GeneralSecurityException {
        InputStream inputStream = new ClassPathResource("static/key/google-api.json").getInputStream();
        credentials = GoogleCredentials
            .fromStream(inputStream)
            .createScoped(AndroidPublisherScopes.ANDROIDPUBLISHER);
        return new AndroidPublisher.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
            new HttpCredentialsAdapter(credentials)
        ).setApplicationName(packageName).build();
    }
}
