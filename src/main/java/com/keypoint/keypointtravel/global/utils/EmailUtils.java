package com.keypoint.keypointtravel.global.utils;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.keypoint.keypointtravel.global.enumType.email.EmailTemplate;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {
    private final JavaMailSender javaMailSender;

    public EmailUtils(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public static boolean sendEmail(String receiver, EmailTemplate template, // json 타입) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            msgHelper.setTo(receiver);
            msgHelper.setSubject(template.getSubject());
            msgHelper.setText(setContext(authNumber, template.getTemplate()), true);
            javaMailSender.send(mimeMessage);

            return true;
        } catch (MessagingException e) {
            throw new GeneralException(CommonErrorCode.FAIL_TO_SEND_EMAIL);
            return false;
        }
    }
}
