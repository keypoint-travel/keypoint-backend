package com.keypoint.keypointtravel.banner.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.keypoint.keypointtravel.banner.entity.AreaCode;
import org.junit.jupiter.api.Test;

public class AreaCodeTest {

    @Test
    public void getAreaCodeTest() {
        //given
        String description = "서울";
        AreaCode expected = AreaCode.SEOUL;
        AreaCode actual = AreaCode.getAreaCode(description);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getDescriptionTest() {
        //given
        String code = "1";
        String expected = "서울";
        String actual = AreaCode.getDescription(code);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}