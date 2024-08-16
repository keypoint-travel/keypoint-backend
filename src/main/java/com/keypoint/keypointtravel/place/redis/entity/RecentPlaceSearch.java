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
    private String searchWord;

    @Convert(converter = TimestampConverter.class)
    private LocalDateTime modifyAt;

    public RecentPlaceSearch(Long memberId, String searchWord) {
        this.memberId = memberId;
        this.searchWord = searchWord;
    }

    public static RecentPlaceSearch of(Long memberId, String searchWord) {
        return new RecentPlaceSearch(memberId, searchWord);
    }

    public void setModifyAt() {
        this.modifyAt = LocalDateTime.now();
    }

}
