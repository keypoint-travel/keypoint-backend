package com.keypoint.keypointtravel.banner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.keypoint.keypointtravel.global.enumType.banner.BannerCode;
import com.keypoint.keypointtravel.global.enumType.banner.LargeCategory;
import org.junit.jupiter.api.Test;

public class LargeCategoryTest {

    @Test
    public void getLargeCategoryTest() {
        //given
        String description = "인문(문화/예술/역사)";
        LargeCategory expected = LargeCategory.CULTURE_ART_HISTORY;
        LargeCategory actual = BannerCode.getConstant(LargeCategory.class, description);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getLargeCategoryDescriptionTest() {
        //given
        String code = "A01";
        String expected = "자연";
        String actual = BannerCode.getDescription(LargeCategory.class, code);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
