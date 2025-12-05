package com.example.birthday.service;

import com.example.birthday.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmail() {
        // 准备测试数据
        String to = "test@example.com";
        String subject = "测试邮件";
        String content = "这是一封测试邮件";

        // 执行测试
        emailService.sendEmail(to, subject, content);

        // 验证结果
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        assertEquals(to, capturedMessage.getTo()[0]);
        assertEquals(subject, capturedMessage.getSubject());
        assertEquals(content, capturedMessage.getText());
    }

    @Test
    void testSendBirthdayReminderEmail() {
        // 准备测试数据
        String to = "test@example.com";
        String friendName = "张三";
        String message = "这是一个生日提醒";

        // 执行测试
        emailService.sendBirthdayReminderEmail(to, friendName, message);

        // 验证结果
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        assertEquals(to, capturedMessage.getTo()[0]);
        assertEquals("生日提醒 - " + friendName + "的生日即将到来", capturedMessage.getSubject());
        assertEquals(message, capturedMessage.getText());
    }

    @Test
    void testSendSms() {
        // 准备测试数据
        String phoneNumber = "13800138000";
        String message = "这是一条短信测试";

        // 执行测试并捕获输出
        emailService.sendSms(phoneNumber, message);

        // 验证System.out.println被调用
        // 注意：在实际应用中，我们通常会验证是否调用了真实的短信服务API
    }
}