package com.keypoint.keypointtravel.global.resolver;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.constants.HeaderConstants;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.LocaleResolver;

@Slf4j
public class CustomLocaleResolver implements LocaleResolver {

    private final MemberDetailRepository memberDetailRepository;

    public CustomLocaleResolver(MemberDetailRepository memberDetailRepository) {
        this.memberDetailRepository = memberDetailRepository;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        try {
            // Accept-Language 헤더 확인
            String locales = request.getHeader(HeaderConstants.LANGUAGE_HEADER);
            if (locales != null && !locales.isBlank()) {
                return LanguageCode.fromCode(locales).getLocale();
            }
            
            // Authorization 헤더 확인
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null
                && authentication.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                LanguageCode languageCode = memberDetailRepository.findLanguageCodeByMemberId(
                    userDetails.getId());

                return languageCode.getLocale();
            }
        } catch (Exception ex) {
            LogUtils.writeErrorLog("resolveLocale", ex.getMessage());
        }

        // 모두 존재하지 않는 경우 영어로 설정
        return LanguageCode.EN.getLocale();
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }

}
