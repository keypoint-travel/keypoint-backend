package com.keypoint.keypointtravel.banner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.keypoint.keypointtravel.banner.entity.BannerCode;
import com.keypoint.keypointtravel.banner.entity.MiddleCategory;
import org.junit.jupiter.api.Test;

public class MiddleCategoryTest {

    @Test
    public void getMiddleCategoryTest() {
        //given
        String description = "가족코스";
        MiddleCategory expected = MiddleCategory.FAMILY_COURSE;
        MiddleCategory actual = BannerCode.getConstant(MiddleCategory.class, description);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getMiddleCategoryDescriptionTest() {
        //given
        String code = "B0201";
        String expected = "숙박시설";
        String actual = BannerCode.getDescription(MiddleCategory.class, code);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
