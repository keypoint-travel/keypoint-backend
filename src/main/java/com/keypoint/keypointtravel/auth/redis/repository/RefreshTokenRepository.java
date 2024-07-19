package com.keypoint.keypointtravel.auth.redis.repository;

import com.keypoint.keypointtravel.auth.dto.dto.RefreshTokenEmailDTO;
import com.keypoint.keypointtravel.auth.redis.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    RefreshTokenEmailDTO findByEmail(String email);
}
