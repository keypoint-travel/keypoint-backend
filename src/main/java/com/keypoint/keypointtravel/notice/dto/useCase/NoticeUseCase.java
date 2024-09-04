package com.keypoint.keypointtravel.notice.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class NoticeUseCase {
    private MultipartFile thumbnailImage;
    private List<MultipartFile> detailImages;
    private LanguageCode language;
    private String title;
    private String content;

}
