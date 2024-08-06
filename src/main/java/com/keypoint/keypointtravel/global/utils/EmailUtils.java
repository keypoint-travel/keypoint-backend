package com.keypoint.keypointtravel.global.utils;

import com.keypoint.keypointtravel.global.enumType.email.EmailTemplate;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 이메일 전송 함수
     *
     * @param receiver     받는 사람
     * @param template     이메일 template
     * @param emailContent 이메일 매핑 내용
     * @return 이메일 성공 여부
     */
    public static void sendEmail(String receiver, EmailTemplate template,
        Map<String, String> emailContent) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            msgHelper.setTo(receiver);
            msgHelper.setSubject(template.getSubject());

            // 템플릿에 매핑된 값을 설정
            Context context = new Context();
            emailContent.forEach(context::setVariable);

            // 템플릿을 처리하여 이메일 본문 생성
            String emailBody = templateEngine.process(template.getTemplate(), context);
            msgHelper.setText(emailBody, true);

            // 이메일 전송
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new GeneralException(CommonErrorCode.FAIL_TO_SEND_EMAIL);
        }
    }
}
