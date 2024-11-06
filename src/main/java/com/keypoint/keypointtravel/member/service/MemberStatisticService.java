package com.keypoint.keypointtravel.member.service;

import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.response.StatisticResponse;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberStatisticService {

    private final MemberRepository memberRepository;

    /**
     * 월별 가입자 수 조회 함수 (createdAt 기준)
     *
     * @return
     */
    public List<StatisticResponse> getMonthlySignUpStatistics() {
        try {
            LocalDateTime startAt = LocalDateTime.now()
                .minusMonths(8)
                .withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

            LocalDateTime endAt = LocalDateTime.now()
                .plusMonths(1)
                .withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

            // 조회
            return memberRepository.findMonthlySignUpStatistics(
                startAt,
                endAt
            );
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    /**
     * 유저 마지막 로그인 통계 함수 (recentLoginAt 기준)
     *
     * @return
     */
    public List<StatisticResponse> getMonthlyLastLoginStatistics() {
        try {
            LocalDateTime startAt = LocalDateTime.now()
                .minusMonths(8)
                .withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

            LocalDateTime endAt = LocalDateTime.now()
                .plusMonths(1)
                .withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

            // 조회
            return memberRepository.findMonthlyLoginStatistics(
                startAt,
                endAt);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    /**
     * 유저 방문 수 통계 함수 (recentLoginAt 기준)
     *
     * @return
     */
    public List<StatisticResponse> getDailyVisitorsStatistics() {
        LocalDateTime startAt = LocalDateTime.now()
            .minusMonths(2)
            .withHour(0).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime endAt = LocalDateTime.now()
            .plusDays(2)
            .withHour(0).withMinute(0).withSecond(0).withNano(0);

        // 조회
        return memberRepository.findDailyVisitorsStatistics(
            startAt,
            endAt
        );
    }
}
