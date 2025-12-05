package com.example.birthday.scheduler;

import com.example.birthday.entity.Friend;
import com.example.birthday.entity.User;
import com.example.birthday.repository.FriendRepository;
import com.example.birthday.repository.UserRepository;
import com.example.birthday.service.EmailService;
import com.example.birthday.service.ReminderService;
import com.example.birthday.util.EmailTemplateUtil;
import com.example.birthday.dto.ReminderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class BirthdayReminderSchedulerTest {

    @InjectMocks
    private BirthdayReminderScheduler scheduler;

    @Mock
    private ReminderService reminderService;

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private EmailTemplateUtil emailTemplateUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckAndSendBirthdayRemindersWithEmail() {
        // 准备测试数据
        List<ReminderDTO> pendingReminders = new ArrayList<>();
        ReminderDTO reminderDTO = new ReminderDTO();
        reminderDTO.setId(UUID.randomUUID().toString());
        reminderDTO.setFriendId(UUID.randomUUID().toString());
        reminderDTO.setType("email"); // 邮件提醒
        pendingReminders.add(reminderDTO);

        Friend friend = new Friend();
        friend.setId(UUID.fromString(reminderDTO.getFriendId()));
        friend.setName("张三");
        friend.setUserId(UUID.randomUUID());

        User user = new User();
        user.setId(friend.getUserId());
        user.setEmail("test@example.com");

        String emailContent = "生日提醒内容";

        // 设置mock行为
        when(reminderService.getPendingReminders(any(LocalDateTime.class))).thenReturn(pendingReminders);
        when(friendRepository.findById(UUID.fromString(reminderDTO.getFriendId()))).thenReturn(Optional.of(friend));
        when(userRepository.findById(friend.getUserId())).thenReturn(Optional.of(user));
        when(emailTemplateUtil.generateBirthdayReminderContent(eq(friend), anyString())).thenReturn(emailContent);

        // 执行测试
        scheduler.checkAndSendBirthdayReminders();

        // 验证结果
        verify(emailService, times(1)).sendBirthdayReminderEmail(user.getEmail(), friend.getName(), emailContent);
        verify(reminderService, times(1)).markAsSent(UUID.fromString(reminderDTO.getId()));
    }

    @Test
    void testCheckAndSendBirthdayRemindersWithSms() {
        // 准备测试数据
        List<ReminderDTO> pendingReminders = new ArrayList<>();
        ReminderDTO reminderDTO = new ReminderDTO();
        reminderDTO.setId(UUID.randomUUID().toString());
        reminderDTO.setFriendId(UUID.randomUUID().toString());
        reminderDTO.setType("sms"); // 短信提醒
        pendingReminders.add(reminderDTO);

        Friend friend = new Friend();
        friend.setId(UUID.fromString(reminderDTO.getFriendId()));
        friend.setName("李四");
        friend.setUserId(UUID.randomUUID());

        User user = new User();
        user.setId(friend.getUserId());
        user.setPhoneNumber("13800138000");

        String smsContent = "生日提醒短信内容";

        // 设置mock行为
        when(reminderService.getPendingReminders(any(LocalDateTime.class))).thenReturn(pendingReminders);
        when(friendRepository.findById(UUID.fromString(reminderDTO.getFriendId()))).thenReturn(Optional.of(friend));
        when(userRepository.findById(friend.getUserId())).thenReturn(Optional.of(user));
        when(emailTemplateUtil.generateSmsBirthdayReminderContent(eq(friend))).thenReturn(smsContent);

        // 执行测试
        scheduler.checkAndSendBirthdayReminders();

        // 验证结果
        verify(emailService, times(1)).sendSms(user.getPhoneNumber(), smsContent);
        verify(reminderService, times(1)).markAsSent(UUID.fromString(reminderDTO.getId()));
    }

    @Test
    void testCheckAndSendBirthdayRemindersWithMissingUserEmail() {
        // 准备测试数据
        List<ReminderDTO> pendingReminders = new ArrayList<>();
        ReminderDTO reminderDTO = new ReminderDTO();
        reminderDTO.setId(UUID.randomUUID().toString());
        reminderDTO.setFriendId(UUID.randomUUID().toString());
        reminderDTO.setType("email");
        pendingReminders.add(reminderDTO);

        Friend friend = new Friend();
        friend.setId(UUID.fromString(reminderDTO.getFriendId()));
        friend.setName("王五");
        friend.setUserId(UUID.randomUUID());

        User user = new User();
        user.setId(friend.getUserId());
        user.setEmail(null); // 用户没有设置邮箱

        // 设置mock行为
        when(reminderService.getPendingReminders(any(LocalDateTime.class))).thenReturn(pendingReminders);
        when(friendRepository.findById(UUID.fromString(reminderDTO.getFriendId()))).thenReturn(Optional.of(friend));
        when(userRepository.findById(friend.getUserId())).thenReturn(Optional.of(user));

        // 执行测试
        scheduler.checkAndSendBirthdayReminders();

        // 验证结果：不会发送邮件，也不会标记为已发送
        verify(emailService, never()).sendBirthdayReminderEmail(anyString(), anyString(), anyString());
        verify(reminderService, never()).markAsSent(any(UUID.class));
    }

    @Test
    void testCheckAndSendBirthdayRemindersWithMissingPhoneNumber() {
        // 准备测试数据
        List<ReminderDTO> pendingReminders = new ArrayList<>();
        ReminderDTO reminderDTO = new ReminderDTO();
        reminderDTO.setId(UUID.randomUUID().toString());
        reminderDTO.setFriendId(UUID.randomUUID().toString());
        reminderDTO.setType("sms");
        pendingReminders.add(reminderDTO);

        Friend friend = new Friend();
        friend.setId(UUID.fromString(reminderDTO.getFriendId()));
        friend.setName("赵六");
        friend.setUserId(UUID.randomUUID());

        User user = new User();
        user.setId(friend.getUserId());
        user.setPhoneNumber(null); // 用户没有设置手机号

        // 设置mock行为
        when(reminderService.getPendingReminders(any(LocalDateTime.class))).thenReturn(pendingReminders);
        when(friendRepository.findById(UUID.fromString(reminderDTO.getFriendId()))).thenReturn(Optional.of(friend));
        when(userRepository.findById(friend.getUserId())).thenReturn(Optional.of(user));

        // 执行测试
        scheduler.checkAndSendBirthdayReminders();

        // 验证结果：不会发送短信，也不会标记为已发送
        verify(emailService, never()).sendSms(anyString(), anyString());
        verify(reminderService, never()).markAsSent(any(UUID.class));
    }
}