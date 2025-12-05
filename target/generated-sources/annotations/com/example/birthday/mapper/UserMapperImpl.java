package com.example.birthday.mapper;

import com.example.birthday.dto.UserDTO;
import com.example.birthday.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-05T16:47:54+0800",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( uuidToString( user.getId() ) );
        userDTO.setPassword( user.getPasswordHash() );
        userDTO.setUsername( user.getUsername() );
        userDTO.setPhoneNumber( user.getPhoneNumber() );
        userDTO.setEmail( user.getEmail() );

        return userDTO;
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( stringToUuid( userDTO.getId() ) );
        user.setPasswordHash( userDTO.getPassword() );
        user.setUsername( userDTO.getUsername() );
        user.setPhoneNumber( userDTO.getPhoneNumber() );
        user.setEmail( userDTO.getEmail() );

        user.setCreatedAt( java.time.LocalDateTime.now() );

        return user;
    }
}
