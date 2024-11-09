package com.keypoint.keypointtravel.inquiry.service;

import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.enumType.error.InquiryErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.inquiry.dto.response.AdminInquiriesResponse;
import com.keypoint.keypointtravel.inquiry.dto.useCase.InquiriesUseCase;
import com.keypoint.keypointtravel.inquiry.dto.useCase.ReplyUseCase;
import com.keypoint.keypointtravel.inquiry.entity.Inquiry;
import com.keypoint.keypointtravel.inquiry.entity.InquiryDetail;
import com.keypoint.keypointtravel.inquiry.entity.InquiryDetailImage;
import com.keypoint.keypointtravel.inquiry.repository.CustomInquiryRepository;
import com.keypoint.keypointtravel.inquiry.repository.InquiryDetailImageRepository;
import com.keypoint.keypointtravel.inquiry.repository.InquiryDetailRepository;
import com.keypoint.keypointtravel.inquiry.repository.InquiryRepository;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AdminInquiryService {

    private final InquiryRepository inquiryRepository;

    private final InquiryDetailRepository inquiryDetailRepository;

    private final InquiryDetailImageRepository inquiryDetailImageRepository;

    private final UploadFileService uploadFileService;

    private final CustomInquiryRepository customInquiryRepository;

    /**
     * 1:1 문의 답변 함수
     *
     * @Param inquiryId, content, images, memberId useCase
     */
    @Transactional
    public void answer(ReplyUseCase useCase) {
        Inquiry inquiry = inquiryRepository.findById(useCase.getInquiryId())
            .orElseThrow(() -> new GeneralException(InquiryErrorCode.NOT_EXISTED_INQUIRY));
        // 0. 문의 사항 검증
        validateInquiry(inquiry);
        // 1. 1:1 문의 내역에 답변 유무를 true로 변경
        inquiry.updateIsReplied(true);
        // 2. 1:1 문의 답변 내용 생성
        InquiryDetail inquiryDetail = new InquiryDetail(inquiry, useCase.getContent(), true);
        inquiryDetailRepository.save(inquiryDetail);
        // 3. 1:1 문의 답변 내 이미지 있을 시, 이미지 저장
        if (useCase.getImages() != null && !useCase.getImages().isEmpty()) {
            useCase.getImages().forEach(image -> {
                // 이미지 저장
                Long imageId = saveUploadFile(image);
                inquiryDetailImageRepository.save(new InquiryDetailImage(inquiryDetail, imageId));
            });
        }
    }

    private void validateInquiry(Inquiry inquiry) {
        // 1. 답변이 완료된 상태인지 검증
        if (inquiry.isReplied()) {
            throw new GeneralException(InquiryErrorCode.ALREADY_REPLIED_INQUIRY);
        }
        // 2. 삭제된 상태(문의한 회원이 삭제)인지 검증
        if (inquiry.isDeleted()) {
            throw new GeneralException(InquiryErrorCode.DELETED_INQUIRY);
        }
    }

    private Long saveUploadFile(MultipartFile image) {
        try {
            return uploadFileService.saveUploadFile(image, DirectoryConstants.INQUIRY_DIRECTORY);
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }

    /**
     * 문의 목록 조회 함수(관리자)
     *
     * @Return AdminInquiriesResponse
     */
    @Transactional(readOnly = true)
    public Page<AdminInquiriesResponse> findInquiries(InquiriesUseCase useCase) {
        try {
            return customInquiryRepository.findInquiriesByAdmin(useCase);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }
}
