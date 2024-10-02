package com.keypoint.keypointtravel.campaign.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "emailRequestHistory")
public class EmailInvitationHistory {

    @Id
    private String id;

    @Indexed
    private Long campaignId;

    @Indexed
    private String email;

    private int count;

    public EmailInvitationHistory(Long campaignId, String email, int count) {
        this.campaignId = campaignId;
        this.email = email;
        this.count = count;
    }

    public void addCount(){
        this.count++;
    }
}
