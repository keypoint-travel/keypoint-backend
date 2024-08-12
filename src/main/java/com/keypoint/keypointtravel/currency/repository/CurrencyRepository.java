package com.keypoint.keypointtravel.currency.repository;

import com.keypoint.keypointtravel.currency.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, String> {
}
