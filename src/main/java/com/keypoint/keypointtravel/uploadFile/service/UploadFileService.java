package com.keypoint.keypointtravel.uploadFile.service;

import com.keypoint.keypointtravel.external.aws.service.S3Service;
import com.keypoint.keypointtravel.global.entity.UploadFile;
import com.keypoint.keypointtravel.uploadFile.repository.UploadFileRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UploadFileService {

    private final UploadFileRepository uploadFileRepository;
    private final S3Service s3Service;

    /**
     * 파일을 S3에 업로드 후, UploadFile에 정보를 업데이트하는 함수
     *
     * @param file          s3에 업로드할 파일
     * @param directoryName 파일을 업로드할 폴더 이름
     * @return 저장된 UploadFile 의 id
     * @throws IOException
     */
    @Transactional
    public Long saveUploadFile(MultipartFile file, String directoryName) throws IOException {
        // 1. s3 파일 업로드
        String fileName = s3Service.uploadFileInS3(file, directoryName);

        // 2. UploadFile 저장
        UploadFile uploadFile = UploadFile.of(fileName, file);
        uploadFileRepository.save(uploadFile);

        return uploadFile.getId();
    }


    /**
     * UploadFile 삭제 함수 (isDeleted를 true로 변경)
     *
     * @param uploadFileId 삭제할 UploadFile 의 id
     */
    @Transactional
    public void deleteUploadFile(Long uploadFileId) {
        uploadFileRepository.updateIsDeletedTrue(uploadFileId);
    }

    /**
     * UploadFile 업데이트 하는 함수
     *
     * @param uploadFileId  업데이트할 id
     * @param file          변경할 파일
     * @param directoryName 새로 저장하는 폴더명
     */
    public void updateUploadFile(Long uploadFileId, MultipartFile file, String directoryName)
        throws IOException {
        // 1. s3 파일 업로드
        String fileName = s3Service.uploadFileInS3(file, directoryName);

        // 2. UploadFile 저장
        uploadFileRepository.updateUploadFile(uploadFileId, fileName, file);
    }
}
