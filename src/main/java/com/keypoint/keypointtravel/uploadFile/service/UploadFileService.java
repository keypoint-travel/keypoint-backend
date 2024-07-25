package com.keypoint.keypointtravel.uploadFile.service;

import com.keypoint.keypointtravel.external.aws.service.S3Service;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.dto.useCase.EmailUseCase;
import com.keypoint.keypointtravel.member.repository.MemberRepository;
import com.keypoint.keypointtravel.uploadFile.repository.UploadFileRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UploadFileService {

    private final UploadFileRepository uploadFileRepository;
    private final S3Service s3Service;

    /**
     * 파일을 S3에 업로드 후, UploadFile에 정보를 업데이트하는 함수
     * 
     * @param file
     * @return
     */
    @Transactional
    public Long saveUploadFile(MultipartFile file, String directroy) {
        String fileName = s3Service.uploadFileInS3(file, directroy);

    }
}
