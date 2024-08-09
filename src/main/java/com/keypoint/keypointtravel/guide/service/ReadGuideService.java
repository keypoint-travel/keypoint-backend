package com.keypoint.keypointtravel.guide.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keypoint.keypointtravel.guide.repository.GuideRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadGuideService {
     private final GuideRepository guideRepository;
}
