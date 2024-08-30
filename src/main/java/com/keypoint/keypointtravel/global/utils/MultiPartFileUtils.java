package com.keypoint.keypointtravel.global.utils;

import java.io.IOException;
import java.nio.file.Files;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultiPartFileUtils {

    public static String convertToBase64(MultipartFile file) throws IOException {
        byte[] image = Base64.encodeBase64(file.getBytes());
        return new String(image);
    }

    public static MultipartFile getImageAsMultipartFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("static/images/" + fileName);

        // 파일을 바이트 배열로 읽음
        byte[] content = Files.readAllBytes(resource.getFile().toPath());

        // MultipartFile로 변환
        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "image/png",
            content);

        return multipartFile;
    }
}
