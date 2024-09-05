package com.keypoint.keypointtravel.global.initializer;

import com.keypoint.keypointtravel.global.enumType.premium.PremiumTitle;
import com.keypoint.keypointtravel.premium.entity.Premium;
import com.keypoint.keypointtravel.premium.repository.PremiumRepository;
import java.util.List;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PremiumInitializer {

    private final PremiumRepository premiumRepository;

    public PremiumInitializer(PremiumRepository premiumRepository) {
        this.premiumRepository = premiumRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void generatePremium() {
        List<Premium> premiums = premiumRepository.findAll();
        if (!premiums.isEmpty()) {
            return;
        }
        Premium premium = Premium.builder()
            .title(PremiumTitle.TWELVE_MONTHS)
            .priceUSD(3)
            .priceKRW(3900)
            .priceJPY(430)
            .durationInMonths(12)
            .build();
        premiumRepository.save(premium);
    }
}
