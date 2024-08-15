package com.keypoint.keypointtravel.place.redis.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "recentPlaceSearch", timeToLive = 86400000L) // 24시간
public class RecentPlaceSearch {

    @Id
    private String id;

    @Indexed
    private Long countryId;

    @Indexed
    private String searchWord;

}
