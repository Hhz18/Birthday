package com.example.birthday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "email",nullable = false,unique = true)
    private String  email;

    @Column(name = "password_Hash",nullable = false)  //非空约束
    private String  passwordHash;

    private LocalDateTime createdAt =LocalDateTime.now();

    @Column(name = "user_name")
    private String username;
    
    @Column(name = "phone_number")
    private String phoneNumber; // 用户手机号，用于短信提醒
}