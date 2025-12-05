package com.example.birthday.repository;

import com.example.birthday.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FriendRepository extends JpaRepository<Friend, UUID> {

    // 查询指定用户的好友
    List<Friend> findByUserId(UUID userId);
}
