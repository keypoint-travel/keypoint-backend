package com.keypoint.keypointtravel.place.controller;

import com.keypoint.keypointtravel.place.service.CityService;
import com.keypoint.keypointtravel.place.service.CountryService;
import com.keypoint.keypointtravel.place.service.PlaceService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceService placeService;
    private final CountryService countryService;
    private final CityService cityService;

    @PostMapping("")
    public ResponseEntity<?> testEvent() throws IOException {
        cityService.generateCityData();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
