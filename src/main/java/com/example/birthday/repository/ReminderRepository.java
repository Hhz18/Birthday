package com.example.birthday.repository;

import com.example.birthday.entity.Friend;
import com.example.birthday.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReminderRepository extends JpaRepository<Reminder, UUID> {
    // 查询指定好友的提醒
    List<Reminder> findByFriend(Friend friend);

    // 查询需要触发但还未发送的提醒（定时任务会用）
    List<Reminder> findByRemindTimeBeforeAndSentFalse(LocalDateTime now);
}
