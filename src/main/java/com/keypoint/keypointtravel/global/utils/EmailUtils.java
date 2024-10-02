package com.keypoint.keypointtravel.global.utils;

import com.keypoint.keypointtravel.global.enumType.email.EmailTemplate;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailUtils {

    private static JavaMailSender javaMailSender;
    private static SpringTemplateEngine templateEngine;


    @Autowired
    public EmailUtils(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        EmailUtils.javaMailSender = javaMailSender;
        EmailUtils.templateEngine = templateEngine;
    }

    /**
     * 이메일 단일 전송 함수
     *
     * @param receiver     받는 사람
     * @param template     이메일 template
     * @param emailContent 이메일 매핑 내용
     * @return 이메일 성공 여부
     */
    public static void sendSingleEmail(
        String receiver,
        EmailTemplate template,
        Map<String, String> emailContent
    ) {
        try {
            // 이메일 전송을 위한 MimeMessageHelper 객체 생성
            MimeMessageHelper msgHelper = prepareMessage(template, emailContent, false);
            Locale currentLocale = LocaleContextHolder.getLocale();
            msgHelper.setSubject(
                MessageSourceUtils.getLocalizedLanguage(
                    template.getSubject(),
                    currentLocale)
            );
            msgHelper.setTo(receiver);
            // 이메일 전송
            javaMailSender.send(msgHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new GeneralException(CommonErrorCode.FAIL_TO_SEND_EMAIL);
        }
    }

    /**
     * 이메일 다중 전송 함수
     *
     * @param receivers    받는 사람
     * @param template     이메일 template
     * @param emailContent 이메일 매핑 내용
     * @return 이메일 성공 여부
     */
    public static void sendMultiEmail(
        List<String> receivers,
        EmailTemplate template,
        Map<String, String> emailContent
    ) {
        try {
            // 이메일 전송을 위한 MimeMessageHelper 객체 생성
            MimeMessageHelper msgHelper = prepareMessage(template, emailContent, false);
            msgHelper.setSubject(template.getSubject());
            msgHelper.setTo(receivers.toArray(new String[receivers.size()]));
            // 이메일 전송
            javaMailSender.send(msgHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new GeneralException(CommonErrorCode.FAIL_TO_SEND_EMAIL);
        }
    }

    /**
     * 이메일 단일 전송 함수(이미지 포함)
     *
     * @param receiver     받는 사람
     * @param template     이메일 template
     * @param emailContent 이메일 매핑 내용
     * @param imagePaths   이메일 이미지 매핑 내용
     * @return 이메일 성공 여부
     */
    public static void sendSingleEmailWithImages(
        String receiver,
        EmailTemplate template,
        Object[] subjectVariables,
        Map<String, String> emailContent, List<String> imagePaths
    ) {
        try {
            // 이메일 전송을 위한 MimeMessageHelper 객체 생성
            MimeMessageHelper msgHelper = prepareMessage(template, emailContent, true);
            msgHelper.setSubject(
                MessageSourceUtils.getLocalizedLanguageWithVariables(template.getSubject(),
                    subjectVariables, LocaleContextHolder.getLocale()));
            msgHelper.setTo(receiver);
            // 이미지 리스트를 반복하고 각 이미지에 대해 addInline 메소드를 호출
            for (int i = 0; i < imagePaths.size(); i++) {
                String imagePath = imagePaths.get(i);
                String contentId = "image" + (i + 1);
                msgHelper.addInline(contentId, new ClassPathResource(imagePath));
            }
            // 이메일 전송
            javaMailSender.send(msgHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new GeneralException(CommonErrorCode.FAIL_TO_SEND_EMAIL);
        }
    }

    /**
     * 이메일 다중 전송 함수(이미지 포함)
     *
     * @param receivers    받는 사람
     * @param template     이메일 template
     * @param emailContent 이메일 매핑 내용
     * @param imagePaths   이메일 이미지 매핑 내용
     * @return 이메일 성공 여부
     */
    public static void sendMultiEmailWithImages(
        List<String> receivers,
        EmailTemplate template,
        Object[] subjectVariables,
        Map<String, String> emailContent, List<String> imagePaths
    ) {
        try {
            // 이메일 전송을 위한 MimeMessageHelper 객체 생성
            MimeMessageHelper msgHelper = prepareMessage(template, emailContent, true);
            msgHelper.setSubject(
                MessageSourceUtils.getLocalizedLanguageWithVariables(template.getSubject(),
                    subjectVariables, LocaleContextHolder.getLocale()));
            msgHelper.setTo(receivers.toArray(new String[receivers.size()]));
            // 이미지 리스트를 반복하고 각 이미지에 대해 addInline 메소드를 호출
            for (int i = 0; i < imagePaths.size(); i++) {
                String imagePath = imagePaths.get(i);
                String contentId = "image" + (i + 1);
                msgHelper.addInline(contentId, new ClassPathResource(imagePath));
            }
            // 이메일 전송
            javaMailSender.send(msgHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new GeneralException(CommonErrorCode.FAIL_TO_SEND_EMAIL);
        }
    }

    private static MimeMessageHelper prepareMessage(EmailTemplate template,
        Map<String, String> emailContent, boolean isMultiFile) throws MessagingException {
        // 이메일 전송을 위한 MimeMessageHelper 객체 생성
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage, isMultiFile, "UTF-8");
        // 템플릿에 매핑된 값을 설정
        Context context = new Context(LocaleContextHolder.getLocale());
        emailContent.forEach(context::setVariable);
        // 템플릿을 처리하여 이메일 본문 생성
        String emailBody = templateEngine.process(template.getTemplate(), context);
        msgHelper.setText(emailBody, true);
        return msgHelper;
    }
}
