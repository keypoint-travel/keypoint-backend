package com.keypoint.keypointtravel.inquiry.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.InquiryErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class ReplyUseCase {

    private Long inquiryId;

    private String content;

    private List<MultipartFile> images;

    private Long memberId;

    public ReplyUseCase(Long inquiryId, String content, List<MultipartFile> images, Long memberId) {
        this.inquiryId = inquiryId;
        this.content = content;
        this.memberId = memberId;
        this.images = new ArrayList<>();
        if(images == null || images.isEmpty()){
            return;
        }
        // 이미지인지 점검
        for (MultipartFile image: images) {
            if (image == null || image.isEmpty() || image.getContentType() == null) {
                continue;
            }
            if (!Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
                throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA);
            }
            this.images.add(image);
        }
        // 이미지가 5개 초과일 경우 예외 처리
        if (this.images.size() > 5) {
            throw new GeneralException(InquiryErrorCode.TOO_MANY_IMAGES);
        }
    }
}
