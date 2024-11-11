package com.keypoint.keypointtravel.member.service;

import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.member.entity.MemberVisitors;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import com.keypoint.keypointtravel.member.repository.memberVisitors.MemberVisitorsRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberVisitorsScheduler {

    private final MemberRepository memberRepository;
    private final MemberVisitorsRepository memberVisitorsRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void saveDailyVisitors() {
        try {
            LocalDate now = LocalDate.now().minusDays(1);
            long visitors = memberRepository.countByRecentLoginAtBetween(
                now.atStartOfDay(),
                now.atTime(LocalTime.MAX));

            MemberVisitors memberVisitors = MemberVisitors.from(visitors);
            memberVisitorsRepository.save(memberVisitors);

            LogUtils.writeInfoLog("saveDailyVisitors",
                "Save daily visitors " + visitors);
        } catch (Exception ex) {
            LogUtils.writeErrorLog("saveDailyVisitors",
                "Fail to save daily visitors", ex);
        }
    }
}
