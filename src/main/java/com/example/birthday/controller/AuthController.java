package com.example.birthday.controller;

import com.example.birthday.common.ApiResponse;
import com.example.birthday.dto.UserDTO;
import com.example.birthday.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 认证模块 Controller
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


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
            if (user == null) {
                return ApiResponse.error(404, "用户不存在或密码错误");
            }
            
            // 验证密码
            if (!userService.validatePassword(userDTO.getEmail(), userDTO.getPassword())) {
                return ApiResponse.error(401, "用户不存在或密码错误");
            }

            // 登录成功，返回用户信息（实际项目中应返回 token）
            return ApiResponse.success("登录成功", user);
        } catch (Exception e) {
            return ApiResponse.error(500, "登录失败: " + e.getMessage());
        }
    }

    /**
     * 用户退出登录
     * POST /auth/logout
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        // 在实际应用中，这里需要从token存储中移除用户的token
        // 由于目前没有实现完整的JWT token机制，这里只是简单返回成功
        return ApiResponse.success("退出登录成功", null);
    }

    /**
     * 查看用户信息
     * GET /auth/user/{id}
     */
    @GetMapping("/user/{id}")
    public ApiResponse<UserDTO> getUserInfo(@PathVariable UUID id) {
        try {
            UserDTO user = userService.getUser(id);
            return ApiResponse.success("查询成功", user);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ApiResponse.error(404, "用户不存在");
            }
            return ApiResponse.error(500, "查询失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户信息
     * PUT /auth/user/{id}
     */
    @PutMapping("/user/{id}")
    public ApiResponse<UserDTO> updateUserInfo(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updated = userService.updateUser(id, userDTO);
            return ApiResponse.success("更新成功", updated);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ApiResponse.error(404, "用户不存在");
            }
            return ApiResponse.error(500, "更新失败: " + e.getMessage());
        }
    }
}