package com.vti.rabbitmqClient.service;

import com.vti.rabbitmqClient.dto.UserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotifyHandler {

    private static final Logger log = LoggerFactory.getLogger(NotifyHandler.class);

    @Autowired
    private JavaMailSender mailSender;

    public void handleUserNotification(String message) {
        UserCreatedEvent event;
        try {
            event = UserCreatedEvent.parse(message);
        } catch (Exception e) {
            log.error("Không parse được message user.created: {}", message, e);
            return;
        }

        log.info("Xử lý thông báo đăng ký tài khoản: userId={}, username={}, email={}",
                event.id(), event.username(), event.email());

        sendWelcomeEmail(event);
    }

    private void sendWelcomeEmail(UserCreatedEvent event) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(event.email());
            mail.setSubject("Chào mừng " + event.fullName() + " đến với hệ thống");
            mail.setText("Xin chào " + event.fullName() + ",\n\n" +
                    "Tài khoản " + event.username() + " của bạn đã được tạo thành công.\n\n" +
                    "Trân trọng.");
            mailSender.send(mail);
            log.info("Đã gửi email chào mừng tới {}", event.email());
        } catch (Exception e) {
            // Không throw — lỗi gửi mail không nên làm crash consumer / mất message
            log.error("Gửi email thất bại tới {}: {}", event.email(), e.getMessage(), e);
        }
    }
}