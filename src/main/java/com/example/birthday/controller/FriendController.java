package com.example.birthday.controller;

import com.example.birthday.common.ApiResponse;
import com.example.birthday.dto.FriendDTO;
import com.example.birthday.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 好友管理模块 Controller
 */
@RestController
@RequestMapping("/friend")
@CrossOrigin(origins = "*")
public class FriendController {

    @Autowired
    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }


    /**
     * 创建好友
     * POST /friend
     */
    @PostMapping
    public ApiResponse<FriendDTO> createFriend(@RequestBody FriendDTO friendDTO) {
        try {
            FriendDTO created = friendService.createFriend(friendDTO);
            return ApiResponse.success("创建成功", created);
        } catch (Exception e) {
            return ApiResponse.error(500, "创建失败: " + e.getMessage());
        }
    }

    /**
     * 查询用户的所有好友
     * GET /friend/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<FriendDTO>> getFriendsByUser(@PathVariable String userId) {
        try {
            List<FriendDTO> friends = friendService.getFriendsByUser(userId);
            return ApiResponse.success("查询成功", friends);
        } catch (Exception e) {
            return ApiResponse.error(500, "查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询好友详情
     * GET /friend/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<FriendDTO> getFriend(@PathVariable String id) {
        //寻找的是
        try {
            FriendDTO friend = friendService.getFriend(id);
            return ApiResponse.success("查询成功", friend);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ApiResponse.error(404, "好友不存在");
            }
            return ApiResponse.error(500, "查询失败: " + e.getMessage());
        }
    }

    /**
     * 更新好友信息
     * PUT /friend/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<FriendDTO> updateFriend(@PathVariable String id, @RequestBody FriendDTO friendDTO) {
        try {
            FriendDTO updated = friendService.updateFriend(id, friendDTO);
            return ApiResponse.success("更新成功", updated);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ApiResponse.error(404, "好友不存在");
            }
            return ApiResponse.error(500, "更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除好友
     * DELETE /friend/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFriend(@PathVariable String id) {
        try {
            //先检查后删除
            friendService.getFriend(id);
            if (id == null || id.isEmpty()) {
                return ApiResponse.error(400, "好友ID不能为空/好友不存在");
            }
            friendService.deleteFriend(id);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(500, "删除失败: " + e.getMessage());
        }
    }
}
