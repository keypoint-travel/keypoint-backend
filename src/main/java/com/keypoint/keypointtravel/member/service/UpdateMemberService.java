package com.keypoint.keypointtravel.member.service;

import com.keypoint.keypointtravel.auth.dto.response.TokenInfoResponse;
import com.keypoint.keypointtravel.badge.respository.BadgeRepository;
import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import com.keypoint.keypointtravel.global.utils.provider.JwtTokenProvider;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.dto.response.AppMemberResponse;
import com.keypoint.keypointtravel.member.dto.useCase.MemberProfileUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.UpdateLanguageUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.UpdateMemberUseCase;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final BadgeRepository badgeRepository;

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
    public AppMemberResponse registerMemberProfile(MemberProfileUseCase useCase) {
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

            // 5. 토큰 데이터 발급
            Authentication authentication = tokenProvider.createAuthenticationFromMember(member,
                RoleType.ROLE_CERTIFIED_USER);
            TokenInfoResponse token = tokenProvider.createToken(authentication);

            String badgeUrl = badgeRepository.findByActiveBadgeUrl(BadgeType.SIGN_UP);
            return AppMemberResponse.of(member, token, badgeUrl);
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
            memberRepository.updatePassword(memberDTO.getId(), passwordEncoder.encode(newPassword));
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
            memberRepository.updateMemberProfile(memberId, useCase.getName(), profileImageId);

        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 회원의 권한을 부여하는 함수
     */
    @Transactional
    public void grandRole() {

    }

    /**
     * 사용자 정보 업데이트 함수
     *
     * @param useCase
     */
    @Transactional
    public void updateMember(UpdateMemberUseCase useCase) {
        CommonMemberDTO member = memberRepository.findByEmailAndIsDeletedFalse(useCase.getEmail())
            .orElseThrow(() -> new GeneralException(MemberErrorCode.NOT_EXISTED_MEMBER));
        RoleType previousRole = member.getRole();
        boolean isChangedRole = member.getRole() != useCase.getRole();
        Long memberId = useCase.getMemberId();

        // 유효성 검사
        validateMember(useCase);

        // 데이터 업데이트
        String encodedPassword = passwordEncoder.encode(useCase.getPassword());
        memberRepository.updateMember(useCase, encodedPassword);

        // 변경되는 권한별 상태 업데이트
        if (isChangedRole) {
            switch (useCase.getRole()) {
                case ROLE_CERTIFIED_USER:
                    handleCertifiedUserRole(memberId);
                case ROLE_ADMIN:
                    handleAdminRole(memberId);
                case ROLE_PENDING_WITHDRAWAL:
                    handlePendingWithdrawalRole(memberId);
            }
        }
    }

    private void handlePendingWithdrawalRole(Long memberId) {
    }

    private void handleAdminRole(Long memberId) {
    }

    private void handleCertifiedUserRole(Long memberId) {
    }

    private void validateMember(UpdateMemberUseCase useCase) {
        // 이메일
        if (memberRepository.existsByEmailAndNotIdAndIsDeletedFalse(useCase.getEmail(),
            useCase.getMemberId())) {
            throw new GeneralException(MemberErrorCode.DUPLICATED_EMAIL);
        }

        //  비밀번호
        if (!StringUtils.checkPasswordValidation(useCase.getPassword())) {
            throw new GeneralException(MemberErrorCode.INVALID_PASSWORD);
        }
    }
}
