package com.keypoint.keypointtravel.global.utils;

import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultiPartFileUtils {

    public static String convertToBase64(MultipartFile file) throws IOException {
        byte[] image = Base64.encodeBase64(file.getBytes());
        return new String(image);
    }

}
