package com.keypoint.keypointtravel.banner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.keypoint.keypointtravel.banner.entity.ContentType;
import org.junit.jupiter.api.Test;

public class ContentTypeTest {

    @Test
    public void getContentTypeTest() {
        //given
        String description = "숙박";
        ContentType expected = ContentType.ACCOMMODATION;
        ContentType actual = ContentType.getContentType(description);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
