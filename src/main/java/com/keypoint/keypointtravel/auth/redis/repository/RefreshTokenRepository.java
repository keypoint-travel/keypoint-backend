package com.keypoint.keypointtravel.auth.redis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.keypoint.keypointtravel.auth.dto.dto.RefreshTokenEmailDTO;
import com.keypoint.keypointtravel.auth.redis.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    RefreshTokenEmailDTO findByEmail(String email);
}