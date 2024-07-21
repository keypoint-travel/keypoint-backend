package com.keypoint.keypointtravel.auth.redis.repository;

import com.keypoint.keypointtravel.auth.redis.entity.OAuthToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthTokenRepository extends CrudRepository<OAuthToken, String> {

    Optional<OAuthToken> findByMemberId(Long memberId);

}
