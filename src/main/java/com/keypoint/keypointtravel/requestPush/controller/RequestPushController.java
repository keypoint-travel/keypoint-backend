package com.keypoint.keypointtravel.requestPush.controller;

import com.keypoint.keypointtravel.requestPush.service.RequestPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/push-requests")
@RequiredArgsConstructor
public class RequestPushController {

    private final RequestPushService requestPushService;
}
