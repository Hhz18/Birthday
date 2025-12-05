package com.example.birthday.service.impl;

import com.example.birthday.dto.FriendDTO;
import com.example.birthday.entity.Friend;
import com.example.birthday.mapper.FriendMapper;
import com.example.birthday.repository.FriendRepository;
import com.example.birthday.repository.UserRepository;
import com.example.birthday.service.FriendService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final FriendMapper friendMapper;

    public FriendServiceImpl(FriendRepository friendRepository, UserRepository userRepository, FriendMapper friendMapper) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.friendMapper = friendMapper;
    }

    @Override
    public FriendDTO createFriend(FriendDTO friendDTO) {
        // 验证用户是否存在
        UUID userId = UUID.fromString(friendDTO.getUserId());
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("用户不存在，无法创建好友");
        }
        
        Friend friend = friendMapper.toEntity(friendDTO);
        Friend savedFriend = friendRepository.save(friend);
        return friendMapper.toDTO(savedFriend);
    }


    @Override
    public FriendDTO updateFriend(String id, FriendDTO dto) {
        Friend friend = friendRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Friend not found"));
        friend.setName(dto.getName());
        friend.setBirthday(dto.getBirthday());
        friend.setGender(dto.getGender());
        friend.setImportance(dto.getImportance());
        friend.setLikes(dto.getLikes());
        friend.setNote(dto.getNote());
        friend.setRemind(dto.getRemind());
        friend.setRemindDays(dto.getRemindDays());
        friend.setUpdatedAt(LocalDateTime.now());
        Friend updatedFriend = friendRepository.save(friend);
        return friendMapper.toDTO(updatedFriend);
    }

    @Override
    public void deleteFriend(String id) {
        friendRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public FriendDTO getFriend(String id) {
        Friend friend = friendRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Friend not found"));
        return friendMapper.toDTO(friend);
    }

    @Override
    public List<FriendDTO> getFriendsByUser(String userId) {
        return friendRepository.findByUserId(UUID.fromString(userId))
                .stream()
                .map(friendMapper::toDTO)
                .toList();
    }
}