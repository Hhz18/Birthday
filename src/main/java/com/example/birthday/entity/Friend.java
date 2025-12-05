package com.example.birthday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Setter
@Getter
@Entity
@Table(name = "friends")
public class Friend {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthday;

    private String gender;

    private Integer importance = 3; // 1-5

    private String likes;

    private String note;

    private Boolean remind = false;

    private Integer remindDays = 3;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    // 外键 user_id
    @Column(nullable = false)
    private UUID userId;
}
