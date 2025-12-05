package com.example.birthday.service;

import com.example.birthday.dto.UserDTO;

import java.util.UUID;

public interface UserService {
    UserDTO register(UserDTO userDTO);

    UserDTO getUser(UUID id);

    UserDTO findByEmail(String email);

    // 密码验证
    boolean validatePassword(String email, String rawPassword);
}
