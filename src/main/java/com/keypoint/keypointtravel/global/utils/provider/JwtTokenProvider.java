package com.keypoint.keypointtravel.global.utils.provider;


import com.keypoint.keypointtravel.auth.dto.response.TokenInfoResponse;
import com.keypoint.keypointtravel.auth.redis.service.RefreshTokenService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.enumType.error.TokenErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements InitializingBean {

    private static final String GRANT_TYPE = "Bearer";
    private static final String ROLE_KEY = "role";
    private static final String ID_KEY = "id";

    private final String jwtSecretKey;
    private final Long accessTokenValidityInMin;
    private final Long refreshTokenValidityInMin;
    private final RefreshTokenService refreshTokenService;
    private String jwtKey;

    public JwtTokenProvider(
        @Value("${key.jwt.secret-key}") String jwtSecretKey,
        @Value("${key.jwt.accesstoken-validity-in-min}") Long accessTokenValidityInMin,
        @Value("${key.jwt.refreshtoken-validity-in-min}") Long refreshTokenValidityInMin,
        RefreshTokenService refreshTokenService) {
        this.jwtSecretKey = jwtSecretKey;
        this.accessTokenValidityInMin = accessTokenValidityInMin * 1000L * 60;
        this.refreshTokenValidityInMin = refreshTokenValidityInMin * 1000L * 60;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String jwtOriginSecretKey = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
        this.jwtKey = jwtOriginSecretKey + jwtOriginSecretKey.substring(0, 30);
    }

    /**
     * JWT token 정보를 생성하는 함수
     *
     * @param authentication 인증 정보가 담긴 객체
     * @return access token과 refresh token이 담긴 jwt 토큰 객체
     */
    public TokenInfoResponse createToken(Authentication authentication) {
        // 1. userID 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        // 2. 권한 가져오기
        String authorities = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        // 3. Access token 생성
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + accessTokenValidityInMin);
        String accessToken = Jwts.builder()
            .setSubject(authentication.getName())
            .claim(ROLE_KEY, authorities)
            .claim(ID_KEY, userId.toString())
            .setExpiration(accessTokenExpiresIn)
            .signWith(SignatureAlgorithm.HS256, jwtKey)
            .compact();

        // 4. Refresh token 생성
        Date refreshTokenExpiration = new Date(now + refreshTokenValidityInMin);
        String refreshToken = Jwts.builder()
            .setExpiration(refreshTokenExpiration)
            .signWith(SignatureAlgorithm.HS256, jwtKey)
            .compact();

        // 5. 토큰 저장
        refreshTokenService.saveRefreshToken(userDetails.getEmail(), refreshToken,
            refreshTokenExpiration);
        return TokenInfoResponse.of(GRANT_TYPE, accessToken, refreshToken);
    }

    /**
     * access token을 복호화하여 토큰에 들어있는 정보를 꺼내는 함수
     *
     * @param accessToken jwt access token
     * @return 인증정보가 담긴 객체
     */
    public Authentication getAuthentication(String accessToken) {
        // 1. 토큰 복호화
        Claims claims = parseClaims(accessToken);

        // 2. 토큰 권한 확인
        if (claims.get(ROLE_KEY) == null) {
            throw new GeneralException(
                HttpStatus.NOT_FOUND,
                TokenErrorCode.INVALID_TOKEN,
                "권한 정보가 없는 토큰입니다."
            );
        }

        // 3. Claim 에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(ROLE_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        // 4. user id 가져오기
        Long userId = Long.parseLong((String) claims.get(ID_KEY));

        // 5. CustomUserDetails 생성
        CustomUserDetails principal = CustomUserDetails.of(
            userId,
            claims.getSubject(),
            authorities
        );

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * access token 만료되었을 때, 갱신을 하기 위해 필요한 정보를 얻기 위한 Claim 반환하는 함수
     *
     * @param accessToken jwt access token
     * @return
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * token의 유효성을 확인하는 함수
     *
     * @param jwtToken jwt 토큰
     * @return 유효성 결과 코드
     */
    public TokenErrorCode validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(jwtToken);
            return TokenErrorCode.NONE;
        } catch (ExpiredJwtException e) {
            return TokenErrorCode.EXPIRED_TOKEN;
        } catch (UnsupportedJwtException e) {
            return TokenErrorCode.UNSUPPORTED_TOKEN;
        } catch (IllegalStateException e) {
            return TokenErrorCode.INVALID_TOKEN;
        }
    }

    /**
     * 토큰 유효시간 반환하는 함수
     *
     * @param accessToken
     * @return 남은 유효 시간 (ms)
     */
    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder()
            .setSigningKey(jwtKey).build()
            .parseClaimsJws(accessToken).getBody()
            .getExpiration();

        if (expiration == null) {
            return 0L;
        }

        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    /**
     * Member 로 Authentication 생성하는 함수
     *
     * @param member
     * @return
     */
    public Authentication createAuthenticationFromMember(CommonMemberDTO member) {
        // 1. Authentication 객체 생성
        CustomUserDetails userDetails = CustomUserDetails.from(member);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails, member.getRole().name()
        );

        return authenticationToken;
    }
}
