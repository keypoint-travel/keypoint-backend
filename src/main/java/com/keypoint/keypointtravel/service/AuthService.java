package com.keypoint.keypointtravel.service;

import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.TokenErrorCode;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import com.keypoint.keypointtravel.global.utils.provider.JwtTokenProvider;
import com.keypoint.keypointtravel.dto.auth.response.TokenInfoDTO;
import com.keypoint.keypointtravel.dto.member.request.LoginRequest;
import com.keypoint.keypointtravel.entity.member.Member;
import com.keypoint.keypointtravel.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
    private final AuthenticationManagerBuilder managerBuilder;

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

    /**
     * 로그인 함수
     *
     * @param request
     * @return
     */
    @Transactional(noRollbackFor = GeneralException.class)
    public TokenInfoDTO login(LoginRequest request) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();

            // 1. 이메일 유효성 검사
            Member user = validateMemberForLogin(email);

            // 2. 비밃번호 유효성 검사
            if (!StringUtils.checkPasswordValidation(password)) {
                throw new GeneralException(MemberErrorCode.INVALID_LOGIN_CREDENTIALS);
            }

            // 3. JWT 토큰 생성
            return getJwtTokenInfo(user.getEmail(), password);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    public TokenInfoDTO getJwtTokenInfo(String email, String password) {
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
     * @param email
     * @return
     */
    private Member validateMemberForLogin(String email) {
        Member member = memberService.findMemberByEmail(email);

        if (member.getOauthProviderType() != OauthProviderType.NONE) {
            throw new GeneralException(MemberErrorCode.REGISTERED_EMAIL_FOR_THE_OTHER);
        }

        return member;
    }
}
