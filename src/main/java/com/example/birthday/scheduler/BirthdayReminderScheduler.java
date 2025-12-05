package com.example.birthday.scheduler;

import com.example.birthday.entity.Friend;
import com.example.birthday.entity.User;
import com.example.birthday.repository.FriendRepository;
import com.example.birthday.repository.UserRepository;
import com.example.birthday.service.EmailService;
import com.example.birthday.service.ReminderService;
import com.example.birthday.util.EmailTemplateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BirthdayReminderScheduler {


    private final ReminderService reminderService;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final EmailTemplateUtil emailTemplateUtil;
    private final EmailService emailService;

    public BirthdayReminderScheduler(ReminderService reminderService, 
                                   UserRepository userRepository,
                                   FriendRepository friendRepository,
                                   EmailTemplateUtil emailTemplateUtil,
                                   EmailService emailService) {
        this.reminderService = reminderService;
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
        this.emailTemplateUtil = emailTemplateUtil;
        this.emailService = emailService;
    }

    /**
     * 定时任务：每小时检查一次是否有需要发送的生日提醒
     * cron表达式：0 0 * * * ? 表示每小时的0分0秒执行
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void checkAndSendBirthdayReminders() {
        try {
            // 获取当前时间之前未发送的提醒
            LocalDateTime now = LocalDateTime.now();
            List<com.example.birthday.dto.ReminderDTO> pendingReminders = reminderService.getPendingReminders(now);
            
            // 遍历并发送邮件
            for (com.example.birthday.dto.ReminderDTO reminderDTO : pendingReminders) {
                // 获取提醒关联的好友信息
                Friend friend = friendRepository.findById(java.util.UUID.fromString(reminderDTO.getFriendId())).orElse(null);
                
                if (friend != null && friend.getUserId() != null) {
                    // 获取用户信息
                    User user = userRepository.findById(friend.getUserId()).orElse(null);
                    
                    if (user != null) {
                        // 根据提醒类型发送邮件或短信
                        if ("sms".equalsIgnoreCase(reminderDTO.getType())) {
                            // 发送短信提醒（需要用户手机号）
                            if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
                                // 生成专门的短信内容
                                String smsContent = emailTemplateUtil.generateSmsBirthdayReminderContent(friend);
                                emailService.sendSms(user.getPhoneNumber(), smsContent);
                                
                                // 标记提醒为已发送
                                reminderService.markAsSent(java.util.UUID.fromString(reminderDTO.getId()));
                            } else {
                                System.out.println("用户未提供手机号，无法发送短信提醒");
                            }
                        } else {
                            // 默认发送邮件提醒
                            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                                // 生成邮件内容
                                String emailContent = emailTemplateUtil.generateBirthdayReminderContent(friend, user.getEmail());
                                emailService.sendBirthdayReminderEmail(user.getEmail(), friend.getName(), emailContent);
                                
                                // 标记提醒为已发送
                                reminderService.markAsSent(java.util.UUID.fromString(reminderDTO.getId()));
                            } else {
                                System.out.println("用户未提供邮箱，无法发送邮件提醒");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
        }
    }
}