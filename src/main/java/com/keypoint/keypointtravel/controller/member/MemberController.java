package com.keypoint.keypointtravel.controller.member;

import com.keypoint.keypointtravel.dto.common.response.APIResponseEntity;
import com.keypoint.keypointtravel.dto.member.request.SignUpRequest;
import com.keypoint.keypointtravel.dto.member.response.MemberDTO;
import com.keypoint.keypointtravel.service.member.MemberService;
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

    @PostMapping("sign-up")
    public APIResponseEntity<MemberDTO> addUser(@Valid @RequestBody SignUpRequest request) {
        MemberDTO userDTO = this.memberService.registerMember(request);

        return APIResponseEntity.<MemberDTO>builder()
            .data(userDTO)
            .build();
    }


}
