package com.keypoint.keypointtravel.banner.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.TourismListUseCase;
import com.keypoint.keypointtravel.banner.entity.AreaCode;
import com.keypoint.keypointtravel.banner.entity.BannerCode;
import com.keypoint.keypointtravel.banner.entity.ContentType;
import com.keypoint.keypointtravel.banner.entity.LargeCategory;
import com.keypoint.keypointtravel.banner.entity.MiddleCategory;
import com.keypoint.keypointtravel.banner.entity.SmallCategory;
import com.keypoint.keypointtravel.global.constants.TourismApiConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TourismApiServiceTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TourismApiService tourismApiService;

    @Test
    public void getTourismList() {
        //given
        int pageNo = 1;
        String region = "인천";
        String tourType = "숙박";
        String cat1 = "숙박";
        String cat2 = "숙박시설";
        String cat3 = "관광호텔";

        //when
        TourismListUseCase useCase = tourismApiService.findTourismList(
            pageNo,
            TourismApiConstants.SERVICE_KEY,
            BannerCode.getConstant(AreaCode.class, region).getCode(),
            BannerCode.getConstant(ContentType.class, tourType).getCode(),
            BannerCode.getConstant(LargeCategory.class, cat1).getCode(),
            BannerCode.getConstant(MiddleCategory.class, cat2).getCode(),
            BannerCode.getConstant(SmallCategory.class, cat3).getCode()
        );

        //then
        assertThat(useCase.getResponse().getBody().getItems().getItem()).isNotEmpty();
    }
}