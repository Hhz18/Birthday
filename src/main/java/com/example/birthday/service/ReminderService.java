package com.example.birthday.service;

import com.example.birthday.dto.ReminderDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReminderService {
    ReminderDTO createReminder(ReminderDTO dto); //创建提醒

    ReminderDTO updateReminder(UUID id, ReminderDTO dto);

    void deleteReminder(UUID id);
    
    ReminderDTO getReminder(UUID id);  // 新增：查询单个提醒

    List<ReminderDTO> getRemindersByFriend(String friendId);  // UUID 改为 String

    List<ReminderDTO> getPendingReminders(LocalDateTime now);
    
    ReminderDTO markAsSent(UUID id);  // 新增：标记为已发送
}
