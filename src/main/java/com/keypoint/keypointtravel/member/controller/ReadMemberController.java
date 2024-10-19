package com.keypoint.keypointtravel.member.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.dto.response.PageResponse;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.member.dto.request.EmailRequest;
import com.keypoint.keypointtravel.member.dto.response.AdminMemberResponse;
import com.keypoint.keypointtravel.member.dto.response.IsExistedResponse;
import com.keypoint.keypointtravel.member.dto.response.MemberSettingResponse;
import com.keypoint.keypointtravel.member.dto.response.memberProfile.MemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.response.otherMemberProfile.OtherMemberProfileResponse;
import com.keypoint.keypointtravel.member.dto.useCase.EmailUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.OtherMemberUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.SearchAdminMemberUseCase;
import com.keypoint.keypointtravel.member.service.ReadMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class ReadMemberController {

    private final ReadMemberService readMemberService;

    @PostMapping("/email/validate")
    public APIResponseEntity<IsExistedResponse> checkIsNotExistedEmail(
        @Valid @RequestBody EmailRequest request) {
        EmailUseCase useCase = EmailUseCase.from(request);
        boolean result = readMemberService.checkIsNotExistedEmail(useCase);

        return APIResponseEntity.<IsExistedResponse>builder()
            .message("등록되지 않은 이메일 확인 성공")
            .data(IsExistedResponse.from(result))
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/profile")
    public APIResponseEntity<MemberProfileResponse> getMemberProfile(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MemberIdUseCase useCase = MemberIdUseCase.from(userDetails.getId());
        MemberProfileResponse result = readMemberService.getMemberProfile(useCase);

        return APIResponseEntity.<MemberProfileResponse>builder()
            .message("사용자 프로필 정보 조회 성공")
            .data(result)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/profile/{memberId}")
    public APIResponseEntity<OtherMemberProfileResponse> getOtherMemberProfile(
        @PathVariable Long memberId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        OtherMemberUseCase useCase = new OtherMemberUseCase(memberId, userDetails);
        OtherMemberProfileResponse result = readMemberService.getOtherMemberProfile(useCase);

        return APIResponseEntity.<OtherMemberProfileResponse>builder()
            .message("다른 회원 프로필 정보 조회 성공")
            .data(result)
            .build();
    }

    /**
     * [관리자] 유저 관리 목록 조회
     *
     * @param sortBy    id
     * @param direction asc, desc
     * @param pageable
     * @return
     */
    @GetMapping("/management")
    public APIResponseEntity<PageResponse<AdminMemberResponse>> findMembersInAdmin(
        @RequestParam(name = "sort-by", required = false) String sortBy,
        @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "role", required = false) RoleType role,
        @PageableDefault(sort = "modifyAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        // sortBy를 제공한 경우, direction 에 따라 정렬 객체 생성
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        SearchAdminMemberUseCase useCase = SearchAdminMemberUseCase.of(
            sortBy,
            direction,
            pageable,
            keyword,
            role
        );
        Page<AdminMemberResponse> result = readMemberService.findMembersInAdmin(useCase);

        return APIResponseEntity.toPage(
            "공지사항 목록 조회 성공",
            result
        );
    }

    @Deprecated
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/settings")
    public APIResponseEntity<MemberSettingResponse> getMemberSetting(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MemberIdUseCase useCase = MemberIdUseCase.from(userDetails.getId());
        MemberSettingResponse result = readMemberService.getMemberSetting(useCase);

        return APIResponseEntity.<MemberSettingResponse>builder()
            .message("사용자 프로필 정보 조회 성공")
            .data(result)
            .build();
    }
}