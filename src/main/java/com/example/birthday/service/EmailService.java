package com.example.birthday.service;

public interface EmailService {
    void sendEmail(String to, String subject, String content);  // 发送普通邮件
    void sendBirthdayReminderEmail(String to, String friendName, String message); // 发送生日提醒邮件
    void sendSms(String phoneNumber, String message); // 添加短信发送功能
}