package com.keypoint.keypointtravel.banner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.keypoint.keypointtravel.banner.entity.AreaCode;
import com.keypoint.keypointtravel.banner.entity.BannerCode;
import org.junit.jupiter.api.Test;

public class AreaCodeTest {

    @Test
    public void getAreaCodeTest() {
        //given
        String description = "서울";
        AreaCode expected = AreaCode.SEOUL;
        AreaCode actual = BannerCode.getConstant(AreaCode.class, description);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getAreaCodeDescriptionTest() {
        //given
        String code = "1";
        String expected = "서울";
        String actual = BannerCode.getDescription(AreaCode.class, code);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}