package com.example.birthday.controller;

import com.example.birthday.common.ApiResponse;
import com.example.birthday.dto.UserDTO;
import com.example.birthday.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证模块 Controller
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    UserService userService;


    /**
     * 用户注册
     * POST /auth/register
     */
    @PostMapping("/register")
    public ApiResponse<UserDTO> register(@RequestBody UserDTO userDTO) {
        try {
            // 检查邮箱是否已存在
            UserDTO existUser = userService.findByEmail(userDTO.getEmail());
            if (existUser != null) {
                return ApiResponse.error(400, "邮箱已被注册");
            }
            
            UserDTO registered = userService.register(userDTO);
            return ApiResponse.success("注册成功", registered);
        } catch (Exception e) {
            return ApiResponse.error(500, "注册失败: " + e.getMessage());
        }
    }

    /**
     * 用户登录
     * POST /auth/login
     */
    @PostMapping("/login")
    public ApiResponse<UserDTO> login(@RequestBody UserDTO userDTO) {
        try {
            UserDTO user = userService.findByEmail(userDTO.getEmail());
//            if (user == null) {
//                return ApiResponse.error(404, "用户不存在或密码错误");
//            }
            
            // 验证密码
            if (!userService.validatePassword(user.getEmail(), userDTO.getPassword())) {
                return ApiResponse.error(401, "用户不存在或密码错误");
            }

            // 登录成功，返回用户信息（实际项目中应返回 token）
            return ApiResponse.success("登录成功", user);
        } catch (Exception e) {
            return ApiResponse.error(500, "登录失败: " + e.getMessage());
        }
    }
}
