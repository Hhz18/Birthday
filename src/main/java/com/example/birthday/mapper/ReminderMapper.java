package com.example.birthday.mapper;

import com.example.birthday.dto.ReminderDTO;
import com.example.birthday.entity.Reminder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ReminderMapper {

    @Mapping(source = "id", target = "id", qualifiedByName = "uuidToString")
    @Mapping(source = "friend.id", target = "friendId", qualifiedByName = "uuidToString")
    @Mapping(source = "userId", target = "userId", qualifiedByName = "uuidToString")
    ReminderDTO toDTO(Reminder reminder);

    @Mapping(source = "id", target = "id", qualifiedByName = "stringToUuid")
    @Mapping(target = "friend", ignore = true) // 在服务层手动设置
    @Mapping(target = "userId", ignore = true) // 通过 PrePersist 自动设置
    Reminder toEntity(ReminderDTO reminderDTO);

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Named("stringToUuid")
    default UUID stringToUuid(String uuid) {
        return uuid != null ? UUID.fromString(uuid) : null;
    }
}
