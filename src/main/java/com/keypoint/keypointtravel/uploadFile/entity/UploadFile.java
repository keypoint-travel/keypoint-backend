package com.keypoint.keypointtravel.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Table(name = "upload_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_file_id")
    private Long id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false)
    private String mimeType;

    @Column(nullable = false)
    private boolean isDeleted;

    public UploadFile(String originalFileName, String path, long size, String mimeType) {
        this.originalFileName = originalFileName;
        this.path = path;
        this.size = size;
        this.mimeType = mimeType;
        this.isDeleted = false;
    }

    public static UploadFile of(String fileName, MultipartFile multipartFile) {
        return new UploadFile(
            multipartFile.getOriginalFilename(),
            fileName,
            multipartFile.getSize(),
            multipartFile.getContentType()
        );
    }
}


