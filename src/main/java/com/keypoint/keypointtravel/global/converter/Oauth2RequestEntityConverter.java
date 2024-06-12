package com.keypoint.keypointtravel.global.converter;

import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
public class Oauth2RequestEntityConverter implements
    Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

    private static final String APPLE_URL = "https://appleid.apple.com";
    private static String appleClientId;
    private static String appleTeamId;
    private static String appleKeyId;
    private static String appleKeyPath;
    private final OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

    @Autowired
    public Oauth2RequestEntityConverter(
        @Value("${spring.security.oauth2.client.registration.apple.clientId}") String appleClientId,
        @Value("${apple.teamId}") String appleTeamId,
        @Value("${apple.keyId}") String appleKeyId,
        @Value("${apple.keyPath}") String appleKeyPath) {
        this.appleClientId = appleClientId;
        this.appleTeamId = appleTeamId;
        this.appleKeyId = appleKeyId;
        this.appleKeyPath = appleKeyPath;
        this.defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    }

    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
        RequestEntity<?> entity = defaultConverter.convert(req);
        String registrationId = req.getClientRegistration().getRegistrationId();
        MultiValueMap<String, String> params = (MultiValueMap<String, String>) entity.getBody();
        if (registrationId.contains("apple")) {
            try {
                params.set("client_secret", createClientSecret());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new RequestEntity<>(
            params,
            entity.getHeaders(),
            entity.getMethod(),
            entity.getUrl()
        );
    }

    public PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/key/" + appleKeyPath);

        InputStream in = resource.getInputStream();
        PEMParser pemParser = new PEMParser(
            new StringReader(IOUtils.toString(in, StandardCharsets.UTF_8)));
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        return converter.getPrivateKey(object);
    }


    public String createClientSecret() throws IOException {
        Date expirationDate = Date.from(
            LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("kid", appleKeyId);
        jwtHeader.put("alg", "ES256");

        return Jwts.builder()
            .setHeaderParams(jwtHeader)
            .setIssuer(appleTeamId)
            .setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간 - UNIX 시간
            .setExpiration(expirationDate) // 만료 시간
            .setAudience(APPLE_URL)
            .setSubject(appleClientId)
            .signWith(getPrivateKey())
            .compact();
    }
}
