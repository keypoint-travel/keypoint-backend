package com.keypoint.keypointtravel.member.dto.response;

import com.keypoint.keypointtravel.global.enumType.member.GenderType;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminMemberResponse {

    private Long userId;
    private String profileImageUrl;
    private String name;
    private String country;
    private String gender;
    private LocalDate birth;
    private String email;
    private String role;
    private LocalDateTime recentRegisterReceiptAt;

    public AdminMemberResponse(
        Long userId,
        String profileImageUrl,
        String name,
        String country,
        GenderType gender,
        LocalDate birth,
        String email,
        RoleType role,
        LocalDateTime recentRegisterReceiptAt
    ) {
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
        this.country = country;
        this.gender = gender != null ? gender.getValue() : null;
        this.birth = birth;
        this.email = email;
        this.role = role != null ? role.getDescription() : null;
        this.recentRegisterReceiptAt = recentRegisterReceiptAt;
    }
}
