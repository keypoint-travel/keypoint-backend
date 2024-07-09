package com.keypoint.keypointtravel.banner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.keypoint.keypointtravel.banner.entity.SmallCategory;
import org.junit.jupiter.api.Test;

public class SmallCategoryTest {

    @Test
    public void getSmallCategoryTest() {
        //given
        String description = "대중콘서트";
        SmallCategory expected = SmallCategory.POP_CONCERT;
        SmallCategory actual = SmallCategory.getSmallCategory(description);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}