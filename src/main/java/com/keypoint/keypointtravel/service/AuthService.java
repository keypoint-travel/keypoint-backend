package com.keypoint.keypointtravel.service;

import com.keypoint.keypointtravel.common.enumType.error.TokenErrorCode;
import com.keypoint.keypointtravel.common.exception.GeneralException;
import com.keypoint.keypointtravel.common.utils.StringUtils;
import com.keypoint.keypointtravel.common.utils.provider.JwtTokenProvider;
import com.keypoint.keypointtravel.dto.auth.response.TokenInfoDTO;
import com.keypoint.keypointtravel.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final static String TOKEN_GRANT_TYPE = "Bearer";

    private final JwtTokenProvider tokenProvider;
    private final MemberService memberService;

    /**
     * JWT 토큰을 재발급하는 함수
     *
     * @param accessToken
     * @param refreshToken
     * @return
     */
    public TokenInfoDTO reissueToken(String accessToken, String refreshToken) {

        try {
            String token = StringUtils.parseGrantTypeInToken(
                TOKEN_GRANT_TYPE,
                accessToken
            );

            // 1. refresh token 유효성 확인
            TokenErrorCode validateResult = tokenProvider.validateToken(refreshToken);
            if (validateResult != TokenErrorCode.NONE) {
                throw new GeneralException(HttpStatus.UNAUTHORIZED, validateResult);
            }

            // 2. Access token 사용자 확인
            Authentication authentication = tokenProvider.getAuthentication(token);

            // 3. JWT 토큰 재발급
            TokenInfoDTO tokenInfoDTO = tokenProvider.createToken(authentication);

            return tokenInfoDTO;
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
