package com.keypoint.keypointtravel.requestPush.service;

import com.keypoint.keypointtravel.requestPush.repository.RequestPushRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestPushService {

    private final RequestPushRepository requestPushRepository;
}
