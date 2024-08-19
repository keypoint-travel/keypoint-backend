package com.keypoint.keypointtravel.campaign.dto.dto;

import jakarta.persistence.Id;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "emailRequestHistory")
public class EmailInvitationHistory {

    @Id
    private String id;

    @Indexed
    private Long campaignLeaderId;

    private String email;

    @TimeToLive(unit = TimeUnit.DAYS)
    private Long expiration;

    public EmailInvitationHistory(Long campaignLeaderId, String email, Long expiration) {
        this.campaignLeaderId = campaignLeaderId;
        this.email = email;
        this.expiration = expiration;
    }
}
