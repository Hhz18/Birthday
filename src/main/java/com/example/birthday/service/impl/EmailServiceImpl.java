package com.example.birthday.service.impl;

import com.example.birthday.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 示例方法，实际实现可能与具体邮件服务提供商的 API 不同,String to: 收件人邮箱,String subject: 邮件主题,String content: 邮件内容
    //处理底层技术细节（创建邮件消息对象、调用邮件发送API等
    @Override
    public void sendEmail(String to, String subject, String content) {
        int maxRetries = 3;
        int retryCount = 0;
        
        while (retryCount < maxRetries) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(content);
                mailSender.send(message);
                System.out.println("邮件发送成功!");
                return; // 成功发送则返回
            } catch (Exception e) {
                retryCount++;
                System.err.println("发送邮件失败 (尝试 " + retryCount + "/" + maxRetries + "): " + e.getMessage());
                
                if (retryCount >= maxRetries) {
                    System.err.println("邮件发送最终失败，已达到最大重试次数");
                    e.printStackTrace();
                    // 在生产环境中，您可能需要将此异常记录到日志系统中
                    // 或者重新抛出一个自定义异常
                } else {
                    // 等待一段时间再重试
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }

    //处理业务逻辑（确定邮件主题格式）
    //这是一种良好的设计模式，将通用的发送邮件功能与特定类型的邮件逻辑分离开来
    @Override
    public void sendBirthdayReminderEmail(String to, String friendName, String message) {
        String subject = "生日提醒 - " + friendName + "的生日即将到来";
        sendEmail(to, subject, message);
    }
    
    @Override
    public void sendSms(String phoneNumber, String message) {
        // 在实际应用中，这里会调用短信服务提供商的 API
        // 例如阿里云短信服务、腾讯云短信服务等
        System.out.println("发送短信到 " + phoneNumber + ": " + message);
        // 实际实现可能类似于：
        // smsService.send(phoneNumber, message);
    }
}