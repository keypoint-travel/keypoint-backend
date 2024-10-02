package com.keypoint.keypointtravel.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
public class LoggingConfig {

    private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void onRequest() {
    }

    @Around("com.keypoint.keypointtravel.global.config.LoggingConfig.onRequest()")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = // 5
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Map<String, Object> requestInfo = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        long start = System.currentTimeMillis();

        try {
            return pjp.proceed(pjp.getArgs());
        } finally {
            long end = System.currentTimeMillis();

            requestInfo.put("Method", request.getMethod());
            requestInfo.put("Remote address", request.getRemoteHost());
            requestInfo.put("URI", request.getRequestURI());
            requestInfo.put("Request parameters", request.getQueryString() == null ? "" : request.getQueryString());
            requestInfo.put("Response time", end - start);

            try {
                List<Object> requestBodyInfo = new ArrayList<>();
                for (Object arg : pjp.getArgs()) {
                    if (arg instanceof MultipartFile) {
                        requestBodyInfo.add(((MultipartFile) arg).getOriginalFilename());
                    } else if (arg instanceof CustomUserDetails) {
                        continue;
                    } else {
                        requestBodyInfo.add(arg);
                    }
                    requestInfo.put("Request body", requestBodyInfo);
                }

                logger.info(objectMapper.writeValueAsString(requestInfo));
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
