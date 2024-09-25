package com.keypoint.keypointtravel.receipt.service;

import com.keypoint.keypointtravel.global.constants.GoogleAPIConstants;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "googleMapAPI", url = GoogleAPIConstants.MAP_URL, fallback = GoogleMapAPIService.class)
public interface GoogleMapAPIService {

}
