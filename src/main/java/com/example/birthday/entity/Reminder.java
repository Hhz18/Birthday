package com.example.birthday.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "reminder_log")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    // 与生日记录的外键对应，一条生日可以有多个提醒
    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private Friend friend;

    // 通过数据库视图或公式获取 user_id
    @Column(name = "user_id")
    private UUID userId;

    // 发送日期
    @Column(name = "send_date", nullable = false)
    private LocalDateTime sendDate;

    // 生日日期
    @Column(name = "birthday_date", nullable = false)
    private LocalDate birthdayDate;

    // 创建时间
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 提醒时间（例如：提前 3 天通知）
    @Column(name = "remind_time")
    private LocalDateTime remindTime;

    // 提醒方式：email、sms (后面可扩展)
    private String type;

    // 提醒内容（简短描述）
    private String message;

    // 是否已发送
    private Boolean sent;

    @PrePersist
    public void prePersist() {
        if (this.friend != null && this.friend.getUserId() != null) {
            this.userId = this.friend.getUserId();
        }
        
        // 设置创建时间为当前时间
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        
        // 如果 sendDate 为空，则设置为 remindTime 的值或者当前时间
        if (this.sendDate == null) {
            this.sendDate = this.remindTime != null ? this.remindTime : LocalDateTime.now();
        }
        
        // 如果 birthdayDate 为空且 friend 不为空，则从 friend 中获取
        if (this.birthdayDate == null && this.friend != null) {
            this.birthdayDate = this.friend.getBirthday();
        }
    }
}
