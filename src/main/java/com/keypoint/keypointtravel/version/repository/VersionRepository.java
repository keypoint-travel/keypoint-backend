package com.keypoint.keypointtravel.version.repository;

import com.keypoint.keypointtravel.version.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {
}
