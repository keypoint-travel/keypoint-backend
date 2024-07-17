package com.keypoint.keypointtravel.banner.controller;

import com.keypoint.keypointtravel.banner.dto.dto.CommentDto;
import com.keypoint.keypointtravel.banner.dto.request.CommentRequest;
import com.keypoint.keypointtravel.banner.dto.response.commonBanner.CommentResponse;
import com.keypoint.keypointtravel.banner.dto.useCase.CreateCommentUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.UpdateCommentUseCase;
import com.keypoint.keypointtravel.banner.service.BannerCommentService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/banners/{bannerId}/comment")
@RequiredArgsConstructor
public class BannerCommentController {

    private final BannerCommentService bannerCommentService;

    @PostMapping
    public ResponseEntity<APIResponseEntity<CommentResponse>> saveComment(
        @PathVariable Long bannerId, @RequestBody @Valid CommentRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        CommentDto dto = bannerCommentService.saveComment(
            new CreateCommentUseCase(bannerId, userDetails.getId(), request.getContent()));

        return ResponseEntity.status(HttpStatus.CREATED).body(
            APIResponseEntity.<CommentResponse>builder()
                .message("bannerId에 해당하는 배너에 댓글 생성")
                .data(CommentResponse.from(dto))
                .build());
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
        @PathVariable Long bannerId, @PathVariable Long commentId, @RequestBody @Valid CommentRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        bannerCommentService.updateComment(
            new UpdateCommentUseCase(bannerId, commentId, userDetails.getId(), request.getContent()));

        return ResponseEntity.noContent().build();
    }
}
