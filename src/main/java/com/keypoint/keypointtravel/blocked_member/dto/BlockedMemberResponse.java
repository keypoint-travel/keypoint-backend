package com.keypoint.keypointtravel.blocked_member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BlockedMemberResponse {

    List<BlockedMemberInfo> blockedMembers;
}