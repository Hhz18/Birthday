package com.example.birthday.dto;

import com.example.birthday.entity.Friend;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReminderDTO {
    private String id; // Reminder 的 UUID 转为 String
    private String userId;
    private String friendId;  // Friend 的 UUID 转为 String
    private LocalDateTime sendDate;
    private LocalDate birthdayDate;
    private LocalDateTime createdAt;
    private LocalDateTime remindTime;
    private String type;
    private String message;
    private Boolean sent;
    private Friend friend; // 关联的好友实体
}
