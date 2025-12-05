package com.example.birthday.util;

import com.example.birthday.entity.Friend;
import org.springframework.stereotype.Component;

@Component
public class EmailTemplateUtil {

    // TODO：生成邮件内容
    public String generateBirthdayReminderContent(Friend friend, String userEmail) {
        StringBuilder content = new StringBuilder();
        content.append("亲爱的用户，\n\n");
        content.append("这是一条来自Birthday生日提醒系统的通知。\n\n");
        content.append("您关注的好友 ");
        content.append(friend.getName());
        content.append(" 的生日即将到来！\n\n");
        
        content.append("好友信息：\n");
        content.append("- 姓名：").append(friend.getName()).append("\n");
        content.append("- 生日：").append(friend.getBirthday()).append("\n");
        content.append("- 提前提醒天数：").append(friend.getRemindDays()).append("天\n\n");
        
        if (friend.getLikes() != null && !friend.getLikes().isEmpty()) {
            content.append("好友喜好：").append(friend.getLikes()).append("\n\n");
        }
        
        if (friend.getNote() != null && !friend.getNote().isEmpty()) {
            content.append("备注：").append(friend.getNote()).append("\n\n");
        }
        
        content.append("请记得为TA准备生日祝福哦！\n\n");
        content.append("此邮件发送至：").append(userEmail).append("\n");
        content.append("Birthday生日提醒系统");
        
        return content.toString();
    }

    //TODO：生成短信内容
    public String generateSmsBirthdayReminderContent(Friend friend) {
        StringBuilder content = new StringBuilder();
        content.append("【Birthday提醒】您关注的好友 ");
        content.append(friend.getName());
        content.append(" 的生日即将到来！记得送上生日祝福哦！");
        
        return content.toString();
    }
}