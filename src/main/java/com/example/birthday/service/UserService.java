package com.example.birthday.service;

import com.example.birthday.dto.UserDTO;

import java.util.UUID;

public interface UserService {
    UserDTO register(UserDTO userDTO);

    UserDTO getUser(UUID id);

    UserDTO findByEmail(String email);

    // 更新用户信息
    UserDTO updateUser(UUID id, UserDTO userDTO);

    // 密码验证
    boolean validatePassword(String email, String rawPassword);
}
