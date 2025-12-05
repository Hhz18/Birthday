package com.example.birthday.util;

import com.example.birthday.entity.Friend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmailTemplateUtilTest {

    private EmailTemplateUtil emailTemplateUtil;

    @BeforeEach
    void setUp() {
        emailTemplateUtil = new EmailTemplateUtil();
    }

    @Test
    void testGenerateBirthdayReminderContent() {
        // 准备测试数据
        Friend friend = new Friend();
        friend.setName("张三");
        friend.setBirthday(LocalDate.of(1990, 5, 15));
        friend.setRemindDays(3);
        friend.setLikes("音乐, 电影");
        friend.setNote("最好的朋友");

        String userEmail = "test@example.com";

        // 执行测试
        String content = emailTemplateUtil.generateBirthdayReminderContent(friend, userEmail);

        // 验证结果
        assertNotNull(content);
        assertTrue(content.contains("亲爱的用户"));
        assertTrue(content.contains("您关注的好友 " + friend.getName() + " 的生日即将到来"));
        assertTrue(content.contains("姓名：" + friend.getName()));
        assertTrue(content.contains("生日：" + friend.getBirthday()));
        assertTrue(content.contains("提前提醒天数：" + friend.getRemindDays() + "天"));
        assertTrue(content.contains("好友喜好：" + friend.getLikes()));
        assertTrue(content.contains("备注：" + friend.getNote()));
        assertTrue(content.contains("此邮件发送至：" + userEmail));
    }

    @Test
    void testGenerateBirthdayReminderContentWithoutOptionalFields() {
        // 准备测试数据（不包含可选字段）
        Friend friend = new Friend();
        friend.setName("李四");
        friend.setBirthday(LocalDate.of(1985, 12, 25));
        friend.setRemindDays(1);

        // likes 和 note 字段保持为 null

        String userEmail = "2307567045@qq.com";

        // 执行测试
        String content = emailTemplateUtil.generateBirthdayReminderContent(friend, userEmail);

        // 验证结果
        assertNotNull(content);
        assertTrue(content.contains("亲爱的用户"));
        assertTrue(content.contains("您关注的好友 " + friend.getName() + " 的生日即将到来"));
        assertTrue(content.contains("姓名：" + friend.getName()));
        assertTrue(content.contains("生日：" + friend.getBirthday()));
        assertTrue(content.contains("提前提醒天数：" + friend.getRemindDays() + "天"));
        assertFalse(content.contains("好友喜好："));
        assertFalse(content.contains("备注："));
        assertTrue(content.contains("此邮件发送至：" + userEmail));
    }

    @Test
    void testGenerateSmsBirthdayReminderContent() {
        // 准备测试数据
        Friend friend = new Friend();
        friend.setName("王五");

        // 执行测试
        String content = emailTemplateUtil.generateSmsBirthdayReminderContent(friend);

        // 验证结果
        assertNotNull(content);
        assertTrue(content.startsWith("【Birthday提醒】"));
        assertTrue(content.contains("您关注的好友 " + friend.getName() + " 的生日即将到来"));
        assertTrue(content.contains("记得送上生日祝福哦！"));
    }
}