package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.member.dto.request.UpdateMemberRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UpdateMemberUseCase {

    private MultipartFile profileImage;
    private Long memberId;
    private String name;
    private String email;
    private String password;
    private RoleType role;

    public static UpdateMemberUseCase of(
        MultipartFile profileImage,
        UpdateMemberRequest request
    ) {
        return new UpdateMemberUseCase(
            profileImage,
            request.getMemberId(),
            request.getName(),
            request.getEmail(),
            request.getPassword(),
            request.getRole()
        );
    }
}
