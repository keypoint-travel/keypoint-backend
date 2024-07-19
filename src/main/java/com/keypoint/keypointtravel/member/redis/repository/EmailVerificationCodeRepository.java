package com.keypoint.keypointtravel.member.redis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keypoint.keypointtravel.member.redis.entity.EmailVerificationCode;

@Repository
public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, String> {
    Optional<EmailVerificationCode> findByEmail(String email);

    Optional<EmailVerificationCode> findByEmailAndCode(String email, String code);
}