package com.keypoint.keypointtravel.place.redis.entity;

import com.keypoint.keypointtravel.global.converter.TimestampConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
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
    private Long memberId;

    @Indexed
    private Long placeId;

    @Convert(converter = TimestampConverter.class)
    private LocalDateTime modifyAt;

    public RecentPlaceSearch(Long memberId, Long placeId) {
        this.memberId = memberId;
        this.placeId = placeId;
    }

    public static RecentPlaceSearch of(Long memberId, Long placeId) {
        return new RecentPlaceSearch(memberId, placeId);
    }

    public void setModifyAt() {
        this.modifyAt = LocalDateTime.now();
    }

}
