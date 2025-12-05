package com.example.birthday.service;

import com.example.birthday.dto.FriendDTO;

import java.util.List;

public interface FriendService {
    FriendDTO createFriend(FriendDTO friendDTO);

    FriendDTO updateFriend(String id, FriendDTO friendDTO);  // UUID 改为 String

    void deleteFriend(String id);  // UUID 改为 String

    FriendDTO getFriend(String id);  // UUID 改为 String

    List<FriendDTO> getFriendsByUser(String userId);  // UUID 改为 String
}
