package com.keypoint.keypointtravel.banner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.keypoint.keypointtravel.banner.entity.MiddleCategory;
import org.junit.jupiter.api.Test;

public class MiddleCategoryTest {

    @Test
    public void getMiddleCategoryTest() {
        //given
        String description = "가족코스";
        MiddleCategory expected = MiddleCategory.FAMILY_COURSE;
        MiddleCategory actual = MiddleCategory.getMiddleCategory(description);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
