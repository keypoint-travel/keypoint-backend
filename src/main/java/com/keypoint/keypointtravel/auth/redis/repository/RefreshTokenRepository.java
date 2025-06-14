package com.keypoint.keypointtravel.auth.redis.repository;

import com.keypoint.keypointtravel.auth.dto.dto.CommonRefreshTokenDTO;
import com.keypoint.keypointtravel.auth.redis.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<CommonRefreshTokenDTO> findByEmail(String email);
}
