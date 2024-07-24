package com.keypoint.keypointtravel.friend.controller;

import com.keypoint.keypointtravel.friend.dto.FriendRequest;
import com.keypoint.keypointtravel.friend.dto.SaveUseCase;
import com.keypoint.keypointtravel.friend.service.FriendService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendController {

    private final FriendService friendService;

    @PostMapping
    public ResponseEntity<Void> saveFriend(
        @RequestBody @Valid FriendRequest friendRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails){

        friendService.saveFriend(new SaveUseCase(friendRequest.getInvitationValue(), userDetails.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
