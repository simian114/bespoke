package com.blog.bespoke.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailUseCase {
    private final JavaMailSender mailSender;
    @Value("${host.url}")
    private String hostUrl;

    public void sendVerificationCode(String email, String code) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("회원가입 이메일");
        mail.setText(String.format("%s/email-validation?code=%s", hostUrl, code));
        mailSender.send(mail);
    }
}
