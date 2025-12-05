package com.example.birthday.service.impl;

import com.example.birthday.dto.UserDTO;
import com.example.birthday.entity.User;
import com.example.birthday.mapper.UserMapper;
import com.example.birthday.repository.UserRepository;
import com.example.birthday.service.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    
    @Override
    public UserDTO register(UserDTO userDTO) {
       User user = userMapper.toEntity(userDTO);
       User savedUser = userRepository.save(user);
       return userMapper.toDTO(savedUser);
    }

    @Override
    public UserDTO getUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user == null ? null : userMapper.toDTO(user);
    }
    
    @Override
    public boolean validatePassword(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        // 在实际应用中，应该使用 BCryptPasswordEncoder 等加密工具来验证密码
        // 这里为了简化演示，直接比较密码字符串
        return user.getPasswordHash().equals(rawPassword);
    }
}
