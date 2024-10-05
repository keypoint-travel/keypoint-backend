package com.keypoint.keypointtravel.auth.redis.service;

import com.keypoint.keypointtravel.auth.dto.dto.CommonRefreshTokenDTO;
import com.keypoint.keypointtravel.auth.redis.entity.RefreshToken;
import com.keypoint.keypointtravel.auth.redis.repository.RefreshTokenRepository;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * jwt 토큰을 관리하기 위해서 refresh token을 저장하는 함수
     *
     * @param email        Member/TempOauthMember의 email
     * @param refreshToken jwt refresh token
     * @param expiration   refresh token의 만료 시간(단위: ms)
     */
    @Transactional
    public void saveRefreshToken(String email, String refreshToken, Date expiration) {
        // 1. email에 등록된 refresh token이 존재하는지 확인
        CommonRefreshTokenDTO refreshTokenDTO = findRefreshTokenByEmail(email);
        if (refreshTokenDTO != null) {
            // 1-1. refresh token이 존재하는 경우 삭제
            String refreshTokenId = refreshTokenDTO.getId();
            refreshTokenRepository.deleteById(refreshTokenId);
        }

        // 2. 새로운 refresh token 생성
        Long remainExpiration = expiration.getTime() - new Date().getTime();
        RefreshToken redisRefreshToken = RefreshToken.of(email, refreshToken, remainExpiration);
        refreshTokenRepository.save(redisRefreshToken);
    }

    /**
     * email로 RefreshToken의 id를 조회하는 함수
     *
     * @param email 찾으려는 refresh token의 email
     * @return refresh token의 id (존재하지 않는 경우: null)
     */
    public CommonRefreshTokenDTO findRefreshTokenByEmail(String email) {
        Optional<CommonRefreshTokenDTO> dtoOptional = refreshTokenRepository.findByEmail(email);

        if (dtoOptional.isPresent()) {
            return dtoOptional.get();
        } else {
            return null;
        }
    }

    /**
     * RefreshToken을 삭제하는 함수
     *
     * @param email 삭제할 토큰 이메일
     * @return
     */
    public void deleteRefreshTokenByEmail(String email) {
        Optional<CommonRefreshTokenDTO> dtoOptional = refreshTokenRepository.findByEmail(email);
        if (dtoOptional.isPresent()) {
            CommonRefreshTokenDTO dto = dtoOptional.get();
            refreshTokenRepository.deleteById(dto.getId());
        }
    }
}
