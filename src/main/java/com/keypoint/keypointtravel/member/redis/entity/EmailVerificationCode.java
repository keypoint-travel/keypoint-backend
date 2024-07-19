package com.keypoint.keypointtravel.member.redis.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@NoArgsConstructor
@RedisHash(value = "emailVerificationCode", timeToLive = 180L) // 5분
public class EmailVerificationCode {

    @Id
    private String id;

    @Indexed
    private String email;
    
    @Indexed
    private String code;

    public EmailVerificationCode(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public static EmailVerificationCode of(String email, String code) {
        return new EmailVerificationCode(email, code);
    }
}
