package com.keypoint.keypointtravel.member.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class SearchAdminMemberUseCase {

    private String sortBy;
    private String direction;
    private Pageable pageable;
    private String keyword;
    private RoleType role;

    public static SearchAdminMemberUseCase of(
        String sortBy,
        String direction,
        Pageable pageable,
        String keyword,
        RoleType role
    ) {
        return new SearchAdminMemberUseCase(
            sortBy,
            direction,
            pageable,
            keyword,
            role
        );
    }

}
