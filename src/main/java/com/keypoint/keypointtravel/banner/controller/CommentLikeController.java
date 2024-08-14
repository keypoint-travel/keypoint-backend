package com.keypoint.keypointtravel.banner.controller;

import com.keypoint.keypointtravel.banner.dto.useCase.ChangeLikeUseCase;
import com.keypoint.keypointtravel.banner.service.CommentLikeService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banners/comment/{commentId}/like")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping
    public ResponseEntity<Void> changeLike(
        @PathVariable Long commentId, @RequestParam Boolean hasILiked,
        @AuthenticationPrincipal CustomUserDetails userDetails){

        commentLikeService.changeLike(new ChangeLikeUseCase(commentId, hasILiked, userDetails.getId()));

        return ResponseEntity.noContent().build();
    }
}
