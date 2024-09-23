package com.keypoint.keypointtravel.auth.service;


import com.keypoint.keypointtravel.auth.dto.dto.CommonRefreshTokenDTO;
import com.keypoint.keypointtravel.auth.dto.response.TokenInfoResponse;
import com.keypoint.keypointtravel.auth.dto.useCase.LogoutUseCase;
import com.keypoint.keypointtravel.auth.dto.useCase.ReissueUseCase;
import com.keypoint.keypointtravel.auth.redis.service.BlacklistService;
import com.keypoint.keypointtravel.auth.redis.service.RefreshTokenService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.TokenErrorCode;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import com.keypoint.keypointtravel.global.utils.provider.JwtTokenProvider;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.dto.useCase.LoginUseCase;
import com.keypoint.keypointtravel.member.service.ReadMemberService;
import com.keypoint.keypointtravel.member.service.UpdateMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private static final String TOKEN_GRANT_TYPE = "Bearer";

    private final JwtTokenProvider tokenProvider;
    private final ReadMemberService readMemberService;
    private final AuthenticationManagerBuilder managerBuilder;
    private final UpdateMemberService updateMemberService;
    private final BlacklistService blacklistService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    /**
     * JWT 토큰을 재발급하는 함수
     *
     * @param useCase
     * @return
     */
    public TokenInfoResponse reissueToken(ReissueUseCase useCase) {
        try {
            String accessToken = useCase.getAccessToken();
            String token = StringUtils.parseGrantTypeInToken(
                TOKEN_GRANT_TYPE,
                accessToken
            );

            // 1. Access token 사용자 확인
            Authentication authentication = tokenProvider.getAuthentication(token);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String email = userDetails.getEmail();

            // 2. refresh token 유효성 확인
            CommonRefreshTokenDTO refreshTokenDTO = refreshTokenService.findRefreshTokenByEmail(
                email);
            if (refreshTokenDTO == null) { // 토큰이 만료되어 존재하지 않는 경우
                throw new GeneralException(HttpStatus.UNAUTHORIZED, TokenErrorCode.EXPIRED_TOKEN);
            }
            String refreshToken = refreshTokenDTO.getRefreshToken();

            // 2-1. 저장된 refresh_token과 요청한 refresh_token이 동일한지 확인
            // 다른 경우: 토큰이 탈취된 경우 
            if (!refreshToken.equals(useCase.getRefreshToken())) {
                throw new GeneralException(HttpStatus.UNAUTHORIZED, TokenErrorCode.INVALID_TOKEN,
                    "저장된 refresh token과 유효하지 않습니다.");
            }

            // 2-2. refresh token 토큰이 유효한지 확인
            TokenErrorCode validateResult = tokenProvider.validateToken(refreshToken);
            if (validateResult != TokenErrorCode.NONE) {
                throw new GeneralException(HttpStatus.UNAUTHORIZED, validateResult);
            }

            // 3. JWT 토큰 재발급
            return tokenProvider.createToken(authentication);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 로그인 함수
     *
     * @param useCase 로그인 정보
     * @return jwt 토큰 정보
     */
    @Transactional
    public TokenInfoResponse login(LoginUseCase useCase) {
        try {
            String email = useCase.getEmail();
            String password = useCase.getPassword();

            // 1. 이메일 유효성 검사
            CommonMemberDTO member = validateMemberForLogin(email);

            // 2. 비밀번호 유효성 검사
            if (!StringUtils.checkPasswordValidation(password)) {
                throw new GeneralException(MemberErrorCode.INVALID_LOGIN_CREDENTIALS);
            }

            // 3. 최근 로그인 일자 업데이트
            updateMemberService.updateRecentLoginAtByMemberId(member.getId());

            // 4. JWT 토큰 생성
            return getJwtTokenInfo(email, password);
        } catch (BadCredentialsException ex) {
            throw new GeneralException(MemberErrorCode.INVALID_PASSWORD);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * JWT 토큰 생성 함수
     *
     * @param email    토큰 생성할 Member의 email
     * @param password 토큰 생성할 Member의 password
     * @return
     */
    public TokenInfoResponse getJwtTokenInfo(String email, String password) {
        // 1. Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            email, password);

        // 2. 실제 검증 진행
        Authentication authentication = managerBuilder.getObject()
            .authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        return tokenProvider.createToken(authentication);
    }

    /**
     * 이메일 유효성 검사를 한 후, 유효성 검사에 통과한 경우, User를 반환하는 함수
     *
     * @param email 유효성 검사할 Member의 email
     * @return 사용자 정보
     */
    private CommonMemberDTO validateMemberForLogin(String email) {
        CommonMemberDTO dto = readMemberService.findMemberByEmail(email);

        if (dto.getOauthProviderType() != OauthProviderType.NONE) {
            throw new GeneralException(MemberErrorCode.REGISTERED_EMAIL_FOR_THE_OTHER);
        }

        return dto;
    }

    /**
     * 로그아웃 함수
     *
     * @param useCase 로그아웃 데이터
     */
    @Transactional
    public void logout(LogoutUseCase useCase) {
        String accessToken = StringUtils.parseGrantTypeInToken(
            TOKEN_GRANT_TYPE,
            useCase.getAccessToken()
        );
        Long expiration = tokenProvider.getExpiration(accessToken);
        if (expiration == null || expiration == 0L) {
            return;
        }

        blacklistService.saveBlacklist(accessToken, expiration);
    }
}
