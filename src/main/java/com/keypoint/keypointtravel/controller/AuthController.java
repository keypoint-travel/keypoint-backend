package com.keypoint.keypointtravel.controller;

import com.keypoint.keypointtravel.common.constants.HeaderConstants;
import com.keypoint.keypointtravel.dto.auth.request.RefreshTokenRequest;
import com.keypoint.keypointtravel.dto.auth.response.TokenInfoDTO;
import com.keypoint.keypointtravel.dto.common.response.APIResponseEntity;
import com.keypoint.keypointtravel.service.AuthService;
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
