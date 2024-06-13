package com.keypoint.keypointtravel.global.config.security.attribute;

import com.keypoint.keypointtravel.entity.member.Member;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthAttributes {

    private static final String GOOGLE_STRING_KEY = "google";
    private static final String APPLE_STRING_KEY = "apple";
    private static final String EMAIL_VERIFIED_KEY = "email_verified";
    private static final String EMAIL_KEY = "email";
    private static final String RESPONSE_KEY = "response";

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String email;
    private OauthProviderType oAuthProvider;

    @Builder
    public OAuthAttributes(
        Map<String, Object> attributes,
        String nameAttributeKey,
        String email,
        OauthProviderType oAuthProvider
    ) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
        this.oAuthProvider = oAuthProvider;
    }

    public static OAuthAttributes of(
        String registrationId,
        String userNameAttributeName,
        Map<String, Object> attributes
    ) {
        if (GOOGLE_STRING_KEY.equals(registrationId)) {
            return ofGoogle(userNameAttributeName, attributes);
        } else {
            return ofApple(userNameAttributeName, attributes);
        }
    }

    private static OAuthAttributes ofGoogle(String nameAttributeKey,
        Map<String, Object> attributes
    ) {
        if (Boolean.FALSE.equals((attributes.get(EMAIL_VERIFIED_KEY)))) {
            throw new GeneralException(MemberErrorCode.NOT_ALLOW_EMAIL);
        }

        return OAuthAttributes.builder()
            .email((String) attributes.get(EMAIL_KEY))
            .oAuthProvider(OauthProviderType.GOOGLE)
            .attributes(attributes)
            .nameAttributeKey(nameAttributeKey)
            .build();
    }

    private static OAuthAttributes ofApple(String nameAttributeKey,
        Map<String, Object> attributes
    ) {
        Map<String, Object> response = (Map<String, Object>) attributes.get(RESPONSE_KEY);

        if (Boolean.FALSE.equals((response.get(EMAIL_VERIFIED_KEY)))) {
            throw new GeneralException(MemberErrorCode.NOT_ALLOW_EMAIL);
        }

        String email = (String) response.get(EMAIL_KEY);
        return OAuthAttributes.builder()
            .email(email)
            .oAuthProvider(OauthProviderType.APPLE)
            .attributes(attributes)
            .nameAttributeKey(nameAttributeKey)
            .build();
    }


    public Member toEntity() {
        return new Member(email, oAuthProvider);
    }
}
