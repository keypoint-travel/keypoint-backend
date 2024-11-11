package com.keypoint.keypointtravel.member.repository.memberVisitors;

import com.keypoint.keypointtravel.member.dto.response.StatisticResponse;
import java.time.LocalDate;
import java.util.List;

public interface MemberVisitorsCustomRepository {

    List<StatisticResponse> findDailyVisitorsStatistics(LocalDate startAt, LocalDate endAt);
}
