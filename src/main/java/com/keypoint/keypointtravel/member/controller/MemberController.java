package com.keypoint.keypointtravel.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keypoint.keypointtravel.auth.dto.response.TokenInfoDTO;
import com.keypoint.keypointtravel.auth.service.AuthService;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.request.LoginRequest;
import com.keypoint.keypointtravel.member.dto.request.SignUpRequest;
import com.keypoint.keypointtravel.member.dto.response.MemberDTO;
import com.keypoint.keypointtravel.member.dto.useCase.LoginUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.SignUpUseCase;
import com.keypoint.keypointtravel.member.service.CreateMemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final CreateMemberService createMemberService;
    private final AuthService authService;

    @PostMapping("sign-up")
    public APIResponseEntity<MemberDTO> addUser(@Valid @RequestBody SignUpRequest request) {
        SignUpUseCase useCase = SignUpUseCase.from(request);
        MemberDTO userDTO = this.createMemberService.registerMember(useCase);

        return APIResponseEntity.<MemberDTO>builder()
            .data(userDTO)
            .build();
    }

    @PostMapping("login")
    public APIResponseEntity<TokenInfoDTO> login(@Valid @RequestBody LoginRequest request) {
        LoginUseCase useCase = LoginUseCase.from(request);
        TokenInfoDTO result = authService.login(useCase);

        return APIResponseEntity.<TokenInfoDTO>builder()
            .data(result)
            .build();
    }
}
