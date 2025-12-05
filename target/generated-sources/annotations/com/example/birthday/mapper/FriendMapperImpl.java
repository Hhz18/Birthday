package com.example.birthday.mapper;

import com.example.birthday.dto.FriendDTO;
import com.example.birthday.entity.Friend;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-05T16:47:54+0800",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class FriendMapperImpl implements FriendMapper {

    @Override
    public FriendDTO toDTO(Friend friend) {
        if ( friend == null ) {
            return null;
        }

        FriendDTO friendDTO = new FriendDTO();

        friendDTO.setId( uuidToString( friend.getId() ) );
        friendDTO.setUserId( uuidToString( friend.getUserId() ) );
        friendDTO.setBirthday( friend.getBirthday() );
        friendDTO.setGender( friend.getGender() );
        friendDTO.setImportance( friend.getImportance() );
        friendDTO.setLikes( friend.getLikes() );
        friendDTO.setName( friend.getName() );
        friendDTO.setNote( friend.getNote() );
        friendDTO.setRemind( friend.getRemind() );
        friendDTO.setRemindDays( friend.getRemindDays() );

        return friendDTO;
    }

    @Override
    public Friend toEntity(FriendDTO friendDTO) {
        if ( friendDTO == null ) {
            return null;
        }

        Friend friend = new Friend();

        friend.setId( stringToUUID( friendDTO.getId() ) );
        friend.setUserId( stringToUUID( friendDTO.getUserId() ) );
        friend.setBirthday( friendDTO.getBirthday() );
        friend.setGender( friendDTO.getGender() );
        friend.setImportance( friendDTO.getImportance() );
        friend.setLikes( friendDTO.getLikes() );
        friend.setName( friendDTO.getName() );
        friend.setNote( friendDTO.getNote() );
        friend.setRemind( friendDTO.getRemind() );
        friend.setRemindDays( friendDTO.getRemindDays() );

        return friend;
    }
}
