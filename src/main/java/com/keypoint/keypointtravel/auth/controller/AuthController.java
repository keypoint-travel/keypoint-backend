package com.keypoint.keypointtravel.auth.controller;

import com.keypoint.keypointtravel.auth.dto.request.RefreshTokenRequest;
import com.keypoint.keypointtravel.auth.dto.response.TokenInfoDTO;
import com.keypoint.keypointtravel.auth.service.AuthService;
import com.keypoint.keypointtravel.global.constants.HeaderConstants;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("reissue")
    public APIResponseEntity<TokenInfoDTO> reissueToken(
        @RequestHeader(value = HeaderConstants.AUTHORIZATION_HEADER) String accessToken,
        @RequestBody RefreshTokenRequest request) {
        TokenInfoDTO result = authService.reissueToken(accessToken, request.getRefreshToken());

        return APIResponseEntity.<TokenInfoDTO>builder()
            .data(result)
            .build();
    }
}
