package com.keypoint.keypointtravel.place.repository;

import com.keypoint.keypointtravel.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceCustomRepository {

    long count();

    boolean existsById(Long id);
}
