package com.example.birthday.mapper;

import com.example.birthday.dto.FriendDTO;
import com.example.birthday.entity.Friend;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    // 实体 -> DTO
    @Mapping(source = "id", target = "id", qualifiedByName = "uuidToString")
    @Mapping(source = "userId", target = "userId", qualifiedByName = "uuidToString")
    FriendDTO toDTO(Friend friend);
    
    // DTO -> 实体
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToUUID")
    @Mapping(source = "userId", target = "userId", qualifiedByName = "stringToUUID")
    Friend toEntity(FriendDTO friendDTO);

    // UUID 转为 String
    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }
    // String 转为 UUID
    @Named("stringToUUID")
    default UUID stringToUUID(String uuid) {
        return uuid != null ? UUID.fromString(uuid) : null;
    }
}
