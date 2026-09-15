package com.vti.rabbitmqClient.service;

import org.springframework.stereotype.Service;

@Service
public class NotifyHandler {
    public void handleUserNotification(String message) {
        System.out.println("Đăng ký tài khoản thành công, gửi thông báo: " + message);
        // TODO: mở rộng gửi email thật, push notification, ghi log, lưu DB...
    }
}