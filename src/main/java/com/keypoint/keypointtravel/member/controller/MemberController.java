package com.keypoint.keypointtravel.member.controller;

import com.keypoint.keypointtravel.auth.dto.response.TokenInfoDTO;
import com.keypoint.keypointtravel.auth.service.AuthService;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.request.LoginRequest;
import com.keypoint.keypointtravel.member.dto.request.SignUpRequest;
import com.keypoint.keypointtravel.member.dto.response.MemberDTO;
import com.keypoint.keypointtravel.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("sign-up")
    public APIResponseEntity<MemberDTO> addUser(@Valid @RequestBody SignUpRequest request) {
        MemberDTO userDTO = this.memberService.registerMember(request);

        return APIResponseEntity.<MemberDTO>builder()
            .data(userDTO)
            .build();
    }

    @PostMapping("login")
    public APIResponseEntity<TokenInfoDTO> login(@Valid @RequestBody LoginRequest request) {
        TokenInfoDTO result = authService.login(request);

        return APIResponseEntity.<TokenInfoDTO>builder()
            .data(result)
            .build();
    }
}
