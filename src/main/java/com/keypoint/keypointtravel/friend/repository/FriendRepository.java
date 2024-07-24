package com.keypoint.keypointtravel.friend.repository;

import com.keypoint.keypointtravel.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long>, FriendCustomRepository {

    Boolean existsByFriendIdAndMemberId(Long friendId, Long memberId);

    List<Friend> findAllByMemberId(Long memberId);
}
