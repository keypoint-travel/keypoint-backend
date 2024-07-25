package com.keypoint.keypointtravel.uploadFile.repository;

import com.keypoint.keypointtravel.global.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

    @Modifying(flushAutomatically = true)
    @Query("UPDATE UploadFile up SET up.isDeleted = true WHERE up.id = :id")
    int updateIsDeletedTrue(@Param("id") Long id);
}
