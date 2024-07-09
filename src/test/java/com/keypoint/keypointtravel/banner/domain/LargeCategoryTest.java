package com.keypoint.keypointtravel.banner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.keypoint.keypointtravel.banner.entity.LargeCategory;
import org.junit.jupiter.api.Test;

public class LargeCategoryTest {

    @Test
    public void getLargeCategoryTest() {
        //given
        String description = "인문(문화/예술/역사)";
        LargeCategory expected = LargeCategory.CULTURE_ART_HISTORY;
        LargeCategory actual = LargeCategory.getLargeCategory(description);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
