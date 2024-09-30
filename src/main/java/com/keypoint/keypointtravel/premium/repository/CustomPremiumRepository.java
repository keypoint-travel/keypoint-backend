package com.keypoint.keypointtravel.premium.repository;

import com.keypoint.keypointtravel.premium.entity.MemberPremium;
import java.util.List;

public interface CustomPremiumRepository {

    List<MemberPremium> findExpiredMemberPremiums();
}
