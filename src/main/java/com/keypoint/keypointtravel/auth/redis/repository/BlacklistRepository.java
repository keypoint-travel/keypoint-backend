package com.keypoint.keypointtravel.auth.redis.repository;

import com.keypoint.keypointtravel.auth.redis.entity.Blacklist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends CrudRepository<Blacklist, String> {

    boolean existsByAccessToken(String accessToken);
}
