package com.keypoint.keypointtravel.banner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.keypoint.keypointtravel.global.enumType.banner.BannerCode;
import com.keypoint.keypointtravel.global.enumType.banner.ContentType;
import org.junit.jupiter.api.Test;

public class ContentTypeTest {

    @Test
    public void getContentTypeTest() {
        //given
        String description = "숙박";
        ContentType expected = ContentType.ACCOMMODATION;
        ContentType actual = BannerCode.getConstant(ContentType.class, description);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getContentTypeDescriptionTest() {
        //given
        String code = "25";
        String expected = "여행코스";
        String actual = BannerCode.getDescription(ContentType.class, code);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
