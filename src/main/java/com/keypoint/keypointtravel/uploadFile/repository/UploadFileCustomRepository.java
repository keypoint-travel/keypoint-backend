package com.keypoint.keypointtravel.uploadFile.repository;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileCustomRepository {

    void updateUploadFile(Long uploadFileId, String fileName, MultipartFile file);
}
