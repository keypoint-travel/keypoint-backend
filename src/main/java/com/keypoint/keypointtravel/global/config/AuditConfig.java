package com.keypoint.keypointtravel.global.config;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class AuditConfig implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = null;
    if (authentication != null
        && authentication.getPrincipal().getClass() == CustomUserDetails.class) {
      userId = ((CustomUserDetails) authentication.getPrincipal()).getId().toString();
    }

    return Optional.ofNullable(userId);
  }
}
