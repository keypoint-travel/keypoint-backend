package com.keypoint.keypointtravel.global.utils;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
        Resource resource = new ClassPathResource("static/images/" + fileName);
        InputStream inputStream = resource.getInputStream();

        // MultipartFile로 변환
        return new MockMultipartFile(fileName, fileName, "image/png", inputStream);
    }
}
