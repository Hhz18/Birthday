package com.example.birthday.controller;

import com.example.birthday.common.ApiResponse;
import com.example.birthday.dto.ReminderDTO;
import com.example.birthday.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 提醒管理模块 Controller
 */
@RestController
@RequestMapping("/reminder")
@CrossOrigin(origins = "*")
public class ReminderController {
    @Autowired
    ReminderService reminderService;

    /**
     * 创建提醒
     * POST /reminder
     */
    @PostMapping
    public ApiResponse<ReminderDTO> createReminder(@RequestBody ReminderDTO reminderDTO) {
        try {
            ReminderDTO created = reminderService.createReminder(reminderDTO);
            return ApiResponse.success("创建成功", created);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Friend not found")) {
                return ApiResponse.error(404, "好友不存在");
            }
            return ApiResponse.error(500, "创建失败: " + e.getMessage());
        }
    }

    /**
     * 查询好友的所有提醒
     * GET /reminder/friend/{friendId}
     */
    @GetMapping("/friend/{friendId}")
    public ApiResponse<List<ReminderDTO>> getRemindersByFriend(@PathVariable String friendId) {
        try {
            List<ReminderDTO> reminders = reminderService.getRemindersByFriend(friendId);
            return ApiResponse.success("查询成功", reminders);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Friend not found")) {
                return ApiResponse.error(404, "好友不存在");
            }
            return ApiResponse.error(500, "查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询提醒详情
     * GET /reminder/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<ReminderDTO> getReminder(@PathVariable UUID id) {
        try {
            ReminderDTO reminder = reminderService.getReminder(id);
            return ApiResponse.success("查询成功", reminder);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ApiResponse.error(404, "提醒不存在");
            }
            return ApiResponse.error(500, "查询失败: " + e.getMessage());
        }
    }

    /**
     * 更新提醒
     * PUT /reminder/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<ReminderDTO> updateReminder(@PathVariable UUID id, @RequestBody ReminderDTO reminderDTO) {
        try {
            ReminderDTO updated = reminderService.updateReminder(id, reminderDTO);
            return ApiResponse.success("更新成功", updated);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ApiResponse.error(404, "提醒不存在");
            }
            return ApiResponse.error(500, "更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除提醒
     * DELETE /reminder/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteReminder(@PathVariable UUID id) {
        try {
            reminderService.deleteReminder(id);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(500, "删除失败: " + e.getMessage());
        }
    }

    /**
     * 查询待发送提醒（定时任务专用）
     * GET /reminder/pending?time=2025-02-01T09:00:00
     */
    @GetMapping("/pending")
    public ApiResponse<List<ReminderDTO>> getPendingReminders(
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
            LocalDateTime time) {
        try {
            LocalDateTime queryTime = time != null ? time : LocalDateTime.now();
            List<ReminderDTO> reminders = reminderService.getPendingReminders(queryTime);
            return ApiResponse.success("查询成功", reminders);
        } catch (Exception e) {
            return ApiResponse.error(500, "查询失败: " + e.getMessage());
        }
    }

    /**
     * 标记提醒为已发送
     * PUT /reminder/{id}/sent
     */
    @PutMapping("/{id}/sent")
    public ApiResponse<ReminderDTO> markAsSent(@PathVariable UUID id) {
        try {
            ReminderDTO updated = reminderService.markAsSent(id);
            return ApiResponse.success("标记成功", updated);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ApiResponse.error(404, "提醒不存在");
            }
            return ApiResponse.error(500, "标记失败: " + e.getMessage());
        }
    }
}
