package com.keypoint.keypointtravel.inquiry.service;

import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.enumType.error.InquiryErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.inquiry.dto.useCase.InquiryUseCase;
import com.keypoint.keypointtravel.inquiry.dto.useCase.ReplyUseCase;
import com.keypoint.keypointtravel.inquiry.entity.Inquiry;
import com.keypoint.keypointtravel.inquiry.entity.InquiryDetail;
import com.keypoint.keypointtravel.inquiry.entity.InquiryDetailImage;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserInquiryService {

    private final MemberRepository memberRepository;

    private final InquiryRepository inquiryRepository;

    private final InquiryDetailRepository inquiryDetailRepository;

    private final InquiryDetailImageRepository inquiryDetailImageRepository;

    private final UploadFileService uploadFileService;

    // 1:1 새 문의하기
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

    private Long saveUploadFile(MultipartFile image) {
        try {
            return uploadFileService.saveUploadFile(image, DirectoryConstants.INQUIRY_DIRECTORY);
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }
}
