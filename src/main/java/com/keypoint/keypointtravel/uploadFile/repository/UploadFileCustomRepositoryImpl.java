package com.keypoint.keypointtravel.uploadFile.repository;

import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class UploadFileCustomRepositoryImpl implements UploadFileCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    @Override
    public void updateUploadFile(Long uploadFileId, String fileName, MultipartFile file) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory.update(uploadFile)
            .set(uploadFile.originalFileName, file.getOriginalFilename())
            .set(uploadFile.path, fileName)
            .set(uploadFile.size, file.getSize())
            .set(uploadFile.mimeType, file.getContentType())

            .set(uploadFile.modifyAt, LocalDateTime.now())
            .set(uploadFile.modifyId, currentAuditor)
            .where(uploadFile.id.eq(uploadFileId))
            .execute();
    }
}
