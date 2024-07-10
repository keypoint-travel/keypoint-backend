package com.keypoint.keypointtravel.banner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.keypoint.keypointtravel.banner.entity.BannerCode;
import com.keypoint.keypointtravel.banner.entity.SmallCategory;
import org.junit.jupiter.api.Test;

public class SmallCategoryTest {

    @Test
    public void getSmallCategoryTest() {
        //given
        String description = "대중콘서트";
        SmallCategory expected = SmallCategory.POP_CONCERT;
        SmallCategory actual = BannerCode.getConstant(SmallCategory.class, description);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getSmallCategoryDescriptionTest() {
        //given
        String code = "B02011200";
        String expected = "홈스테이";
        String actual = BannerCode.getDescription(SmallCategory.class, code);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}