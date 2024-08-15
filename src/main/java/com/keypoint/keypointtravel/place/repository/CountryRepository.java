package com.keypoint.keypointtravel.place.repository;

import com.keypoint.keypointtravel.place.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

}
