package com.keypoint.keypointtravel.external.aws.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final static String UPLOAD_FILE_NAME_FORMAT = "yyMMdd";
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String s3BucketName;

    /**
     * S3에 파일을 업로드 후, 업로드 된 URL을 반환받는 함수
     *
     * @param file          업로드할 파일
     * @param directoryPath 업로드할 폴더 위치
     * @return
     * @throws IOException
     */
    public String uploadFileInS3(MultipartFile file, String directoryPath) throws IOException {
        // 1. MultipartFile을 File로 변환
        File uploadFile = convertAndSaveInLocal(file)
            .orElseThrow(() -> new GeneralException(CommonErrorCode.FAIL_TO_CONVERT_FILE));

        // 2. 업로드할 파일명 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(UPLOAD_FILE_NAME_FORMAT);
        String createdFileName =
            LocalDateTime.now().format(formatter) + StringUtils.generateUniqueNumber();
        String fillExtension = StringUtils.getFileExtension(file.getOriginalFilename())
            .orElse(null);
        String fileName =
            (fillExtension == null) ? String.format("%s/%s", directoryPath, createdFileName) :
                String.format("%s/%s.%s", directoryPath, createdFileName, fillExtension);

        // 3. S3에 업로드
        return putFileInS3(uploadFile, fileName);
    }

    /**
     * S3에 파일 업로드하는 함수
     */
    private String putFileInS3(File uploadFile, String fileName) {
        try {
            amazonS3Client.putObject(
                new PutObjectRequest(s3BucketName, fileName, uploadFile).withCannedAcl(
                    CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(s3BucketName, fileName).toString();
        } catch (Exception ex) {
            throw new GeneralException(ex);
        } finally {
            removeFileInLocal(uploadFile);
        }
    }


    /**
     * MultipartFile을 File로 변환 후, Local에 저장하는 함수
     *
     * @param file
     * @return
     * @throws IOException
     */
    private Optional<File> convertAndSaveInLocal(MultipartFile file) throws IOException {
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    /**
     * Local에 저장되어 있는 파일을 삭제하는 함수
     *
     * @param targetFile
     */
    private void removeFileInLocal(File targetFile) {
        targetFile.delete();
    }

    /**
     * s3에 업로드한 파일 삭제 함수
     *
     * @param fileName uploadFileInS3 업로드 시, 전달 받은 filename의 uri
     * @return true: 삭제 성공 / false: 삭제 실패
     */
    public boolean deleteFileInS3(String fileName) {
        try {
            boolean isObjectExist = amazonS3Client.doesObjectExist(s3BucketName, fileName);

            // S3에서 파일 삭제
            if (isObjectExist) {
                amazonS3Client.deleteObject(s3BucketName, fileName);
            }

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * s3에 업로드한 파일의 다운로드 링크 생성
     *
     * @param fileUrl uploadFileInS3 업로드 시, 전달 받은 filename의 url, 만료 시간(시간 딘위)
     * @return 다운로드 링크 url(String)
     */
    public String generatePreSignedUrl(String fileUrl, Long expirationInHours, String reportName)
        throws Exception {
        // 1. Url에서 파일 경로 추출
        String fileName = extractFileNameFromUrl(fileUrl);
        // 2. 만료 시간 설정
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60 * expirationInHours; // 시간 단위 설정
        expiration.setTime(expTimeMillis);
        // 3. Pre-signed URL 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest = generatePresignedUrlRequest(
            fileName, expiration, reportName);
        // 4. 응답
        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    private String extractFileNameFromUrl(String fileUrl) throws Exception {
        URL url = new URL(fileUrl);
        String filePath = url.getPath();
        return filePath.startsWith("/") ? filePath.substring(1) : filePath;
    }

    private GeneratePresignedUrlRequest generatePresignedUrlRequest(String fileName,
        Date expiration, String reportName) {
        return new GeneratePresignedUrlRequest(s3BucketName, fileName)
            .withMethod(HttpMethod.GET)
            .withExpiration(expiration)
            .withResponseHeaders(new ResponseHeaderOverrides().withContentDisposition(
                "attachment; filename=\"" + reportName + "\"")); // 파일 이름 추가
    }
}
