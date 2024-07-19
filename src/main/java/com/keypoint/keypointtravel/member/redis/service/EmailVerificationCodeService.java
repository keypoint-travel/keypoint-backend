package com.keypoint.keypointtravel.member.redis.service;

import com.keypoint.keypointtravel.member.redis.entity.EmailVerificationCode;
import com.keypoint.keypointtravel.member.redis.repository.EmailVerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailVerificationCodeService {

    private final EmailVerificationCodeRepository emailAuthenticationNumberRepository;

    /**
     * 이메일 인증 데이터를 저장하는 함수
     *
     * @param email
     * @param code
     */
    @Transactional
    public void saveEmailVerificationCode(String email, String code) {
        // 1. email 에 대해서 인증 데이터가 존재하는지 확인
        EmailVerificationCode existedCode = findEmailVerificationCodeByEmail(email);
        if (existedCode != null) {
            // 1-1. 삭제
            deleteEmailVerificationCode(existedCode);
        }

        // 2. 저장
        EmailVerificationCode emailVerificationCode = EmailVerificationCode.of(email, code);
        emailAuthenticationNumberRepository.save(emailVerificationCode);
    }

    /**
     * email에 대해서 EmailVerificationCode를 조회하는 함수
     *
     * @param email
     * @return
     */
    public EmailVerificationCode findEmailVerificationCodeByEmail(String email) {
        return emailAuthenticationNumberRepository.findByEmail(email).orElse(null);
    }

    /**
     * 전달받은 email, code에 대해서 EmailVerificationCode를 조회하는 함수
     *
     * @param email
     * @param code
     * @return
     */
    public EmailVerificationCode findEmailVerificationCodeByEmailAndCode(String email,
        String code) {
        return emailAuthenticationNumberRepository.findByEmailAndCode(email, code).orElse(null);
    }

    /**
     * emailVerificationCode 삭제하는 함수
     *
     * @param emailVerificationCode
     */
    public void deleteEmailVerificationCode(EmailVerificationCode emailVerificationCode) {
        emailAuthenticationNumberRepository.delete(emailVerificationCode);
    }

}
