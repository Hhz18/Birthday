package com.example.birthday.integration;

import com.example.birthday.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendRealEmail() {
        // 注意：这会向您的真实邮箱发送邮件
        // 请确保在测试环境中使用，避免频繁发送
        emailService.sendEmail(
            "2307567045@qq.com", 
            "生日提醒系统测试邮件", 
            "这是一封来自Birthday生日提醒系统的测试邮件，用于验证邮件发送功能是否正常工作。"
        );
        
        // 等待几秒钟确保邮件发送完成
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}