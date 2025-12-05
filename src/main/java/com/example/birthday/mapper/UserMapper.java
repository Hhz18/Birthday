package com.example.birthday.mapper;


import com.example.birthday.dto.UserDTO;
import com.example.birthday.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id", qualifiedByName = "uuidToString")
    @Mapping(source = "passwordHash", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    UserDTO toDTO(User user);

    @Mapping(source = "id", target = "id", qualifiedByName = "stringToUuid")
    @Mapping(source = "password", target = "passwordHash")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    User toEntity(UserDTO userDTO);

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Named("stringToUuid")
    default UUID stringToUuid(String uuid) {
        return uuid != null ? UUID.fromString(uuid) : null;
    }
}