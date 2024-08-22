package com.keypoint.keypointtravel.member.service;

import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.dto.response.MemberResponse;
import com.keypoint.keypointtravel.member.dto.useCase.MemberProfileUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.UpdateLanguageUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.UpdatePasswordUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.UpdateProfileUseCase;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.entity.MemberConsent;
import com.keypoint.keypointtravel.member.entity.MemberDetail;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.member.repository.memberConsent.MemberConsentRepository;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import com.keypoint.keypointtravel.notification.entity.Notification;
import com.keypoint.keypointtravel.notification.repository.notification.NotificationRepository;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UpdateMemberService {

    private final MemberRepository memberRepository;
    private final MemberConsentRepository memberConsentRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final NotificationRepository notificationRepository;
    private final ReadMemberService readMemberService;
    private final UploadFileService uploadFileService;


    /**
     * 최근 로그인 날짜 정보를 업데이트하는 함수
     *
     * @param memberId 최근 로그인 날짜 정보를 업데이트 사용자 아이디
     */
    @Transactional
    public void updateRecentLoginAtByMemberId(Long memberId) {
        memberRepository.updateRecentLoginAtByMemberId(memberId, LocalDateTime.now());
    }

    /**
     * Member 개인 정보 생성하는 함수 (소셜 로그인)
     *
     * @param useCase 개인 정보 데이터
     * @return
     */
    @Transactional
    public MemberResponse registerMemberProfile(MemberProfileUseCase useCase) {
        try {
            // 1. Member 찾기
            Member member = memberRepository.findById(useCase.getMemberId())
                .orElseThrow(() -> new GeneralException(MemberErrorCode.NOT_EXISTED_MEMBER));

            // 2. Member 상태 변경
            memberRepository.updateRole(member.getId(), RoleType.ROLE_CERTIFIED_USER);

            // 3. MemberConsent, MemberDetail, Notification 생성
            MemberConsent memberConsent = MemberConsent.from(member);
            MemberDetail memberDetail = useCase.toEntity(member);
            Notification notification = Notification.from(member);

            // 4. 저장
            memberConsentRepository.save(memberConsent);
            memberDetailRepository.save(memberDetail);
            notificationRepository.save(notification);

            return MemberResponse.from(member);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 회원 비밀번호를 변경하는 함수
     *
     * @param useCase
     */
    @Transactional
    public void updateMemberPassword(UpdatePasswordUseCase useCase) {
        try {
            String email = useCase.getEmail();
            String newPassword = useCase.getNewPassword();

            // 1. 소셜 로그인 등록 사용자가 아닌지 확인
            CommonMemberDTO memberDTO = readMemberService.findMemberByEmail(email);
            if (memberDTO.getOauthProviderType() != OauthProviderType.NONE) {
                throw new GeneralException(MemberErrorCode.NOT_GENERAL_MEMBER);
            }

            // 2. 비밀번호 변경
            memberRepository.updatePassword(memberDTO.getId(), newPassword);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 회원 선택 언어를 변경하는 함수
     *
     * @param useCase 회원 및 변경 언어 정보 데이터
     */
    @Transactional
    public void updateMemberLanguage(UpdateLanguageUseCase useCase) {
        try {
            // 1. 언어 변경 시도
            memberDetailRepository.updateLanguage(useCase.getMemberId(), useCase.getLanguage());

            // 2. 연결된 사용자 기기에 FCM 알림 전달
            // TODO
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 회원 프로필 데이터 변경하는 함수
     *
     * @param useCase 회원 프로필 데이터
     */
    @Transactional
    public void updateMemberProfile(UpdateProfileUseCase useCase) {
        try {
            Long memberId = useCase.getMemberId();

            // 1. (기존 프로필 이미지가 존재한 경우) 기존 프로필 이미지 삭제
            Optional<Long> profileImageIdOptional = memberDetailRepository.findProfileImageIdByMemberId(
                memberId);
            if (profileImageIdOptional.isPresent()) {
                uploadFileService.deleteUploadFile(profileImageIdOptional.get());
            }

            // 2. 프로필 이미지가 존재하는 경우, 신규 프로필 이미지 저장
            Long profileImageId = null;
            if (useCase.getProfileImage() != null) {
                profileImageId = uploadFileService.saveUploadFile(
                    useCase.getProfileImage(),
                    DirectoryConstants.MEMBER_PROFILE_DIRECTORY
                );
            }
            // 3. 프로필 데이터 변경
            memberDetailRepository.updateMemberProfile(memberId, useCase.getName(), profileImageId);

        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

}
