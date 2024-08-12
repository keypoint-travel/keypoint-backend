package config;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@TestConfiguration
public class TestConfig implements AuditorAware<String> {


    // AuditorAware
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

    // QueryDSL
    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
    }
}
