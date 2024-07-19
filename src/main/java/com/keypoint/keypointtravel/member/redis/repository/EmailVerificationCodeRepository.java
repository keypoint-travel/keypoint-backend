package com.keypoint.keypointtravel.member.redis.repository;

import com.keypoint.keypointtravel.member.redis.entity.EmailVerificationCode;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationCodeRepository extends
    CrudRepository<EmailVerificationCode, String> {

    Optional<EmailVerificationCode> findByEmail(String email);

    Optional<EmailVerificationCode> findByEmailAndCode(String email, String code);
}