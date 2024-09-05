package com.keypoint.keypointtravel.version.repository;

import com.keypoint.keypointtravel.global.enumType.setting.VersionType;
import com.keypoint.keypointtravel.version.dto.response.VersionResponse;
import com.keypoint.keypointtravel.version.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {

    @Query("SELECT v.type FROM Version v")
    Set<VersionType> findVersionType();

    @Query("SELECT new com.keypoint.keypointtravel.version.dto.response.VersionResponse(v.type, v.version)" +
            "FROM Version v")
    List<VersionResponse> findCommonVersions();
}
