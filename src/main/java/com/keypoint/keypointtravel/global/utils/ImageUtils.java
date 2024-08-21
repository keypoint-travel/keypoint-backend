package com.keypoint.keypointtravel.global.utils;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.springframework.stereotype.Component;

@Component
public class ImageUtils {

    public static BufferedImage convertImageUrlToImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);

            // URL에서 이미지 읽기
            return ImageIO.read(url);
        } catch (Exception e) {
            throw new GeneralException(CommonErrorCode.FAILED_TO_CONVERT_IMAGE);
        }
    }

    public static String getMimeType(BufferedImage image) {
        try (ImageInputStream iis = ImageIO.createImageInputStream(image)) {
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
            if (imageReaders.hasNext()) {
                ImageReader reader = imageReaders.next();
                return "image/" + reader.getFormatName().toLowerCase();
            }
        } catch (Exception ex) {
            return "image/unknown";
        }
        return "image/unknown";
    }
}
