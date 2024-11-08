package com.keypoint.keypointtravel.inquiry.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class UserInquiryUserCase {

    private String content;

    private List<MultipartFile> images;

    private Long memberId;

    public UserInquiryUserCase(String content, List<MultipartFile> images, Long memberId) {
        this.content = content;
        this.memberId = memberId;
        this.images = new ArrayList<>();
        if(images.isEmpty() || images == null){
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
    }
}
