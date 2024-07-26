package com.keypoint.keypointtravel.friend.controller;

import com.keypoint.keypointtravel.friend.dto.FriendRequest;
import com.keypoint.keypointtravel.friend.dto.FriendsResponse;
import com.keypoint.keypointtravel.friend.dto.SaveUseCase;
import com.keypoint.keypointtravel.friend.service.FriendService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendController {

    private final FriendService friendService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping
    public ResponseEntity<Void> saveFriend(
        @RequestBody @Valid FriendRequest friendRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails){

        friendService.saveFriend(new SaveUseCase(friendRequest.getInvitationValue(), userDetails.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping
    public APIResponseEntity<FriendsResponse> findFriendList(@AuthenticationPrincipal CustomUserDetails userDetails){

        FriendsResponse result = friendService.findFriendList(userDetails.getId());
        return APIResponseEntity.<FriendsResponse>builder()
            .message("친구 목록 조회 성공")
            .data(result)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> deleteFriend(
        @PathVariable Long friendId,
        @AuthenticationPrincipal CustomUserDetails userDetails){

        friendService.deleteFriend(userDetails.getId(), friendId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
