package com.keypoint.keypointtravel.member.controller;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.response.StatisticResponse;
import com.keypoint.keypointtravel.member.service.MemberStatisticService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members/statistics")
public class MemberStatisticController {

    private static MemberStatisticService memberStatisticService;

    @GetMapping("/monthly-signups")
    public APIResponseEntity<List<StatisticResponse>> getMonthlySignUpStatistics() {
        List<StatisticResponse> result = memberStatisticService.getMonthlySignUpStatistics();

        return APIResponseEntity.<List<StatisticResponse>>builder()
            .message("월별 가입자 수 조회 성공")
            .data(result)
            .build();
    }

    @GetMapping("/monthly-last-login")
    public APIResponseEntity<List<StatisticResponse>> getMonthlyLastLoginStatistics() {
        List<StatisticResponse> result = memberStatisticService.getMonthlyLastLoginStatistics();

        return APIResponseEntity.<List<StatisticResponse>>builder()
            .message("유저 마지막 로그인 수 조회 성공")
            .data(result)
            .build();
    }

    @GetMapping("/daily-visitors")
    public APIResponseEntity<List<StatisticResponse>> getDailyVisitorsStatistics() {
        List<StatisticResponse> result = memberStatisticService.getDailyVisitorsStatistics();

        return APIResponseEntity.<List<StatisticResponse>>builder()
            .message("유저 방문수 성공")
            .data(result)
            .build();
    }
}
