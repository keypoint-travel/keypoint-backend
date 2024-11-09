package com.keypoint.keypointtravel.inquiry.service;

import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.enumType.error.InquiryErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.inquiry.dto.response.UserInquiriesResponse;
import com.keypoint.keypointtravel.inquiry.dto.useCase.InquiryUseCase;
import com.keypoint.keypointtravel.inquiry.dto.useCase.ReplyUseCase;
import com.keypoint.keypointtravel.inquiry.entity.Inquiry;
import com.keypoint.keypointtravel.inquiry.entity.InquiryDetail;
import com.keypoint.keypointtravel.inquiry.entity.InquiryDetailImage;
import com.keypoint.keypointtravel.inquiry.repository.CustomInquiryRepository;
import com.keypoint.keypointtravel.inquiry.repository.InquiryDetailImageRepository;
import com.keypoint.keypointtravel.inquiry.repository.InquiryDetailRepository;
import com.keypoint.keypointtravel.inquiry.repository.InquiryRepository;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserInquiryService {

    private final MemberRepository memberRepository;

    private final InquiryRepository inquiryRepository;

    private final InquiryDetailRepository inquiryDetailRepository;

    private final InquiryDetailImageRepository inquiryDetailImageRepository;

    private final UploadFileService uploadFileService;

    private final CustomInquiryRepository customInquiryRepository;

    /**
     * 1:1 문의 생성 함수
     *
     * @Param content, images, memberId useCase
     */
    @Transactional
    public void inquire(InquiryUseCase useCase) {
        // 1. 1:1 문의 생성
        Member member = memberRepository.getReferenceById(useCase.getMemberId());
        Inquiry inquiry = new Inquiry(member, useCase.getContent());
        inquiryRepository.save(inquiry);
        // 2. 1:1 문의 내용 생성
        InquiryDetail inquiryDetail = new InquiryDetail(inquiry, useCase.getContent(), false);
        inquiryDetailRepository.save(inquiryDetail);
        // 3. 1:1 문의 내 이미지 있을 시, 이미지 저장
        if (useCase.getImages() != null && !useCase.getImages().isEmpty()) {
            useCase.getImages().forEach(image -> {
                // 이미지 저장
                Long imageId = saveUploadFile(image);
                inquiryDetailImageRepository.save(new InquiryDetailImage(inquiryDetail, imageId));
            });
        }
    }

    /**
     * 1:1 문의 응답 답변 함수
     *
     * @Param inquiryId, content, images, memberId useCase
     */
    @Transactional
    public void answer(ReplyUseCase useCase) {
        Inquiry inquiry = inquiryRepository.findById(useCase.getInquiryId())
            .orElseThrow(() -> new GeneralException(InquiryErrorCode.NOT_EXISTED_INQUIRY));
        // 0. 문의 사항 검증
        validateInquiry(inquiry, useCase.getMemberId());
        // 1. 1:1 문의 내역에 답변 유무를 false로 변경
        inquiry.updateIsReplied(false);
        // 2. 1:1 문의 내용 생성
        InquiryDetail inquiryDetail = new InquiryDetail(inquiry, useCase.getContent(), false);
        inquiryDetailRepository.save(inquiryDetail);
        // 3. 1:1 문의 내 이미지 있을 시, 이미지 저장
        if (useCase.getImages() != null && !useCase.getImages().isEmpty()) {
            useCase.getImages().forEach(image -> {
                // 이미지 저장
                Long imageId = saveUploadFile(image);
                inquiryDetailImageRepository.save(new InquiryDetailImage(inquiryDetail, imageId));
            });
        }
    }

    private Long saveUploadFile(MultipartFile image) {
        try {
            return uploadFileService.saveUploadFile(image, DirectoryConstants.INQUIRY_DIRECTORY);
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }

    private void validateInquiry(Inquiry inquiry,Long memberId) {
        // 1. 본인이 작성한 문의 사항인지 검증
        if (!Objects.equals(inquiry.getMember().getId(), memberId)) {
            throw new GeneralException(InquiryErrorCode.NOT_MATCHED_MEMBER);
        }
        // 2. 아직 관리자 답변이 없는지 검증(없다면 예외 처리)
        if (!inquiry.isReplied()) {
            throw new GeneralException(InquiryErrorCode.NOT_REPLIED_INQUIRY);
        }
        // 3. 삭제된 문의사항인지 검증
        if (inquiry.isDeleted()) {
            throw new GeneralException(InquiryErrorCode.DELETED_INQUIRY);
        }
    }

    /**
     * 사용자 문의 목록 조회 함수
     *
     * @Param memberId
     * @Return UserInquiriesResponse
     */
    @Transactional(readOnly = true)
    public List<UserInquiriesResponse> findInquiries(Long memberId) {
        return customInquiryRepository.findInquiriesByUser(memberId);
    }
}
