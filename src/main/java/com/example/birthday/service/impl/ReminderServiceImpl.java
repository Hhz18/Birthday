package com.example.birthday.service.impl;

import com.example.birthday.dto.ReminderDTO;
import com.example.birthday.entity.Friend;
import com.example.birthday.entity.Reminder;
import com.example.birthday.mapper.ReminderMapper;
import com.example.birthday.repository.FriendRepository;
import com.example.birthday.repository.ReminderRepository;
import com.example.birthday.service.EmailService;
import com.example.birthday.service.ReminderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReminderServiceImpl implements ReminderService {
    private final ReminderRepository reminderRepository;
    private final FriendRepository friendRepository;
    private final ReminderMapper reminderMapper;


    public ReminderServiceImpl(ReminderRepository reminderRepository, FriendRepository friendRepository, 
                              ReminderMapper reminderMapper, EmailService emailService) {
        this.reminderRepository = reminderRepository;
        this.friendRepository = friendRepository;
        this.reminderMapper = reminderMapper;
    }

    @Override
    public ReminderDTO createReminder(ReminderDTO dto) {
        Reminder reminder = reminderMapper.toEntity(dto);
        // 需要为 friend 赋值实体对象
        Friend friend = friendRepository.findById(UUID.fromString(dto.getFriendId()))
                .orElseThrow(() -> new RuntimeException("Friend not found"));
        reminder.setFriend(friend);
        reminder.setSent(false); // 新建提醒默认未发送

        Reminder saved = reminderRepository.save(reminder);
        return reminderMapper.toDTO(saved);
    }

    @Override
    public ReminderDTO updateReminder(UUID id, ReminderDTO dto) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));
        // 更新字段
        reminder.setRemindTime(dto.getRemindTime());
        reminder.setType(dto.getType());
        reminder.setMessage(dto.getMessage());
        reminder.setSent(dto.getSent());

        Reminder updated = reminderRepository.save(reminder);
        return reminderMapper.toDTO(updated);
    }

    @Override
    public void deleteReminder(UUID id) {
        reminderRepository.deleteById(id);
    }

    @Override
    public ReminderDTO getReminder(UUID id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));
        return reminderMapper.toDTO(reminder);
    }

    @Override
    public List<ReminderDTO> getRemindersByFriend(String friendId) {
        Friend friend = friendRepository.findById(UUID.fromString(friendId))
                .orElseThrow(() -> new RuntimeException("Friend not found"));
        List<Reminder> reminders = reminderRepository.findByFriend(friend);
        return reminders.stream()
                .map(reminderMapper::toDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<ReminderDTO> getPendingReminders(LocalDateTime now) {
        List<Reminder> reminders = reminderRepository.findByRemindTimeBeforeAndSentFalse(now);
        return reminders.stream()
                .map(reminderMapper::toDTO)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public ReminderDTO markAsSent(UUID id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));
        reminder.setSent(true);
        Reminder updated = reminderRepository.save(reminder);
        return reminderMapper.toDTO(updated);
    }
}