package com.keypoint.keypointtravel.friend.repository;

import com.keypoint.keypointtravel.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long>, FriendCustomRepository {

    Boolean existsByFriendId(Long friendId);
}
