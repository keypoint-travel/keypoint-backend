package com.keypoint.keypointtravel.auth.controller;

import com.keypoint.keypointtravel.auth.dto.request.LoginRequest;
import com.keypoint.keypointtravel.auth.dto.request.RefreshTokenRequest;
import com.keypoint.keypointtravel.auth.dto.response.TokenInfoResponse;
import com.keypoint.keypointtravel.auth.dto.useCase.LogoutUseCase;
import com.keypoint.keypointtravel.auth.dto.useCase.ReissueUseCase;
import com.keypoint.keypointtravel.auth.service.AuthService;
import com.keypoint.keypointtravel.global.constants.HeaderConstants;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.useCase.LoginUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public APIResponseEntity<TokenInfoResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginUseCase useCase = LoginUseCase.from(request);
        TokenInfoResponse result = authService.login(useCase);

        return APIResponseEntity.<TokenInfoResponse>builder()
            .message("로그인 성공")
            .data(result)
            .build();
    }

    @PostMapping("reissue")
    public APIResponseEntity<TokenInfoResponse> reissueToken(
        @RequestHeader(value = HeaderConstants.AUTHORIZATION_HEADER) String accessToken,
        @Valid @RequestBody RefreshTokenRequest request) {
        ReissueUseCase useCase = ReissueUseCase.of(accessToken, request.getRefreshToken());
        TokenInfoResponse result = authService.reissueToken(useCase);

        return APIResponseEntity.<TokenInfoResponse>builder()
            .data(result)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("logout")
    public APIResponseEntity<Void> logout(
        @RequestHeader(value = HeaderConstants.AUTHORIZATION_HEADER) String accessToken
    ) {
        LogoutUseCase useCase = LogoutUseCase.from(accessToken);
        authService.logout(useCase);

        return APIResponseEntity.<Void>builder()
            .message("로그아웃 성공")
            .data(null)
            .build();
    }
}
