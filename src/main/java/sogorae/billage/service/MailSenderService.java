package sogorae.billage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MailSenderService {

    private static final String MAIL_TITLE = "안녕하세요. Billage입니다.";

    private final JavaMailSender javaMailSender;

    public void send(String email, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(MAIL_TITLE);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }
}
