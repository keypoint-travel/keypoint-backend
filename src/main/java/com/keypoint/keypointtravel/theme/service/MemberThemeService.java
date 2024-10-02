package com.keypoint.keypointtravel.theme.service;

import com.keypoint.keypointtravel.global.enumType.error.ThemeErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import com.keypoint.keypointtravel.theme.dto.response.MemberThemeResponse;
import com.keypoint.keypointtravel.theme.dto.useCase.MemberThemeUseCase;
import com.keypoint.keypointtravel.theme.entity.PaidTheme;
import com.keypoint.keypointtravel.theme.entity.Theme;
import com.keypoint.keypointtravel.theme.repository.PaidThemeRepository;
import com.keypoint.keypointtravel.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberThemeService {

    private final ThemeRepository themeRepository;
    private final PaidThemeRepository paidThemeRepository;
    private final MemberDetailRepository memberDetailRepository;

    /**
     * 테마 설정
     *
     * @param useCase
     */
    @Transactional
    public void updateTheme(MemberThemeUseCase useCase) {
        try {
            memberDetailRepository.clearThemes(useCase.getMemberId());
            boolean paid = useCase.isPaid();
            if (paid){
                PaidTheme theme = paidThemeRepository.findByIdAndIsDeletedFalse(useCase.getThemeId())
                    .orElseThrow(() -> new GeneralException(ThemeErrorCode.NOT_EXISTED_Theme));
                memberDetailRepository.updatePaidTheme(useCase.getMemberId(), theme);
            }
            else {
                Theme theme = themeRepository.findByIdAndIsDeletedFalse(useCase.getThemeId())
                    .orElseThrow(() -> new GeneralException(ThemeErrorCode.NOT_EXISTED_Theme));
                memberDetailRepository.updateTheme(useCase.getMemberId(), theme);

            }
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 유저 테마 정보 조회
     *
     * @param useCase
     * @return
     */
    public MemberThemeResponse findThemeInMember(MemberIdUseCase useCase) {
        try {
            MemberThemeResponse memberThemeResponse = themeRepository.findThemeByUserId(
                useCase.getMemberId()
            );

            if (memberThemeResponse == null) {
                throw new GeneralException(ThemeErrorCode.NOT_EXISTED_Member_Theme);
            }

            return memberThemeResponse;
        } catch (Exception ex) {
            log.error("Error occurred while finding themes: ", ex);
            throw new GeneralException(ex);
        }
    }

}
