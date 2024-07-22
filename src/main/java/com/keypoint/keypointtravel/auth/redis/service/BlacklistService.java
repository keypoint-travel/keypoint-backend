package com.keypoint.keypointtravel.auth.redis.service;

import com.keypoint.keypointtravel.auth.redis.entity.Blacklist;
import com.keypoint.keypointtravel.auth.redis.repository.BlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;

    /**
     * blacklist 저장 함수
     *
     * @param accessToken
     * @param expiration
     */
    @Transactional
    public void saveBlacklist(String accessToken, Long expiration) {
        Blacklist blacklist = new Blacklist(accessToken, expiration);
        blacklistRepository.save(blacklist);
    }

    public boolean existsBlacklistByAccessToken(String accessToken) {
        return blacklistRepository.existsByAccessToken(accessToken);
    }
}