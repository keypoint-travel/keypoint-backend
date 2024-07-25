package com.keypoint.keypointtravel.member.service;

import com.keypoint.keypointtravel.global.enumType.email.EmailTemplate;
import com.keypoint.keypointtravel.global.enumType.error.MemberErrorCode;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.EmailService;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import com.keypoint.keypointtravel.member.dto.dto.CommonMemberDTO;
import com.keypoint.keypointtravel.member.dto.response.MemberResponse;
import com.keypoint.keypointtravel.member.dto.useCase.EmailUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.EmailVerificationUseCase;
import com.keypoint.keypointtravel.member.dto.useCase.SignUpUseCase;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.entity.MemberConsent;
import com.keypoint.keypointtravel.member.entity.MemberDetail;
import com.keypoint.keypointtravel.member.redis.entity.EmailVerificationCode;
import com.keypoint.keypointtravel.member.redis.service.EmailVerificationCodeService;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.member.repository.memberConsent.MemberConsentRepository;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import com.keypoint.keypointtravel.notification.entity.Notification;
import com.keypoint.keypointtravel.notification.repository.NotificationRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreateMemberService {

    private static final int EMAIL_VERIFICATION_CODE_DIGITS = 6;

    private final MemberRepository memberRepository;
    private final MemberConsentRepository memberConsentRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationCodeService emailVerificationCodeService;
    private final EmailService emailService;

    /**
     * Member 생성하는 함수 (일반 회원가입)
     *
     * @param useCase 회원 가입 데이터
     * @return
     */
    @Transactional
    public MemberResponse registerMember(SignUpUseCase useCase) {
        try {
            String email = useCase.getEmail();
            String password = useCase.getPassword();

            // 1. 이메일 유효성 검사
            validateEmailForSignUp(email);

            // 2. 비밀번호 유효성 검사
            if (!StringUtils.checkPasswordValidation(password)) {
                throw new GeneralException(MemberErrorCode.INVALID_PASSWORD);
            }

            // 3. Member, MemberConsent, MemberDetail, Notification 생성
            Member member = Member.of(email, encodePassword(password));
            MemberConsent memberConsent = MemberConsent.from(member);
            MemberDetail memberDetail = useCase.toEntity(member);
            Notification notification = Notification.from(member);

            // 4. 저장
            memberRepository.save(member);
            memberConsentRepository.save(memberConsent);
            memberDetailRepository.save(memberDetail);
            notificationRepository.save(notification);

            //5. 초대코드 저장: 초대코드가 중복되지 않도록 문자열에 마지막에 memberId를 추가하여 저장
            member.setInvitationCode(StringUtils.getRandomString(9) + member.getId());

            return MemberResponse.from(member);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 비밀번호를 PasswordEncoder로 암호화
     *
     * @param password 암호화할 비밀번호
     * @return
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * (일반 회원가입 경우) 계정 유효성 검사
     *
     * @param email 이메일
     */
    private void validateEmailForSignUp(String email) {
        Optional<CommonMemberDTO> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            CommonMemberDTO existMember = memberOptional.get();
            if (existMember.getOauthProviderType() == OauthProviderType.NONE) {
                throw new GeneralException(MemberErrorCode.DUPLICATED_EMAIL);
            } else {
                throw new GeneralException(MemberErrorCode.REGISTERED_EMAIL_FOR_THE_OTHER);
            }
        }
    }

    /**
     * 이메일 인증 코드 전달 함수
     *
     * @param useCase 이메일 정보 데이터
     * @return
     */
    public void sendVerificationCodeToEmail(EmailUseCase useCase) {
        try {
            String email = useCase.getEmail();

            // 1. 인증 코드 생성
            String code = StringUtils.getRandomNumber(EMAIL_VERIFICATION_CODE_DIGITS);

            // 2. 이메일 전송
            Map<String, String> emailContent = new HashMap<>();
            emailContent.put("code", code);
            emailService.sendEmail(email, EmailTemplate.EMAIL_VERIFICATION, emailContent);

            // 3. 이메일 전송이 성공일 경우, 인증 코드 저장
            emailVerificationCodeService.saveEmailVerificationCode(email, code);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 이메일 인증 확인 결과를 전달하는 함수
     *
     * @param useCase 이메일 인증 데이터
     */
    public boolean confirmEmail(EmailVerificationUseCase useCase) {
        String email = useCase.getEmail();
        String code = useCase.getCode();

        try {
            EmailVerificationCode emailVerificationCode = emailVerificationCodeService
                .findEmailVerificationCodeByEmailAndCode(email, code);
            boolean result = emailVerificationCode != null;
            if (emailVerificationCode != null) {
                emailVerificationCodeService.deleteEmailVerificationCode(emailVerificationCode);
            }

            return result;
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
