package com.keypoint.keypointtravel.place.repository;

import com.keypoint.keypointtravel.global.enumType.PlaceType;
import com.keypoint.keypointtravel.place.entity.Place;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceCustomRepository {

    long count();

    boolean existsById(Long id);

    List<Place> findByPlaceTypeOrderByCityId(PlaceType placeType);
}
