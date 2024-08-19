package com.keypoint.keypointtravel.global.constants;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PlaceConstants {

    public static List<String> MAYOR_COUNTRIES = Arrays.asList(
        "KR",
        "FR",
        "ES",
        "US",
        "CN",
        "IT",
        "TR",
        "MX",
        "DE",
        "JP",
        "PT",
        "AU",
        "CA"
    );

    public static int MAX_PLACE_SEARCH_WORD_CNT = 5;
}
