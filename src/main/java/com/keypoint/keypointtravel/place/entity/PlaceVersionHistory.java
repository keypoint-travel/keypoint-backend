package com.keypoint.keypointtravel.place.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "place_version_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceVersionHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "place_version_history_id")
  private Long id;

  @Column(unique = true)
  private String version;

  public PlaceVersionHistory(String version) {
    this.version = version;
  }

  public static PlaceVersionHistory from(String version) {
    return new PlaceVersionHistory(version);
  }
  
}
