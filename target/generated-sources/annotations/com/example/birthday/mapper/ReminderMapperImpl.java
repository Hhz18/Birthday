package com.example.birthday.mapper;

import com.example.birthday.dto.ReminderDTO;
import com.example.birthday.entity.Friend;
import com.example.birthday.entity.Reminder;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-05T16:47:54+0800",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ReminderMapperImpl implements ReminderMapper {

    @Override
    public ReminderDTO toDTO(Reminder reminder) {
        if ( reminder == null ) {
            return null;
        }

        ReminderDTO reminderDTO = new ReminderDTO();

        reminderDTO.setId( uuidToString( reminder.getId() ) );
        reminderDTO.setFriendId( uuidToString( reminderFriendId( reminder ) ) );
        reminderDTO.setUserId( uuidToString( reminder.getUserId() ) );
        reminderDTO.setFriend( reminder.getFriend() );
        reminderDTO.setBirthdayDate( reminder.getBirthdayDate() );
        reminderDTO.setCreatedAt( reminder.getCreatedAt() );
        reminderDTO.setMessage( reminder.getMessage() );
        reminderDTO.setRemindTime( reminder.getRemindTime() );
        reminderDTO.setSendDate( reminder.getSendDate() );
        reminderDTO.setSent( reminder.getSent() );
        reminderDTO.setType( reminder.getType() );

        return reminderDTO;
    }

    @Override
    public Reminder toEntity(ReminderDTO reminderDTO) {
        if ( reminderDTO == null ) {
            return null;
        }

        Reminder reminder = new Reminder();

        reminder.setId( stringToUuid( reminderDTO.getId() ) );
        reminder.setBirthdayDate( reminderDTO.getBirthdayDate() );
        reminder.setCreatedAt( reminderDTO.getCreatedAt() );
        reminder.setMessage( reminderDTO.getMessage() );
        reminder.setRemindTime( reminderDTO.getRemindTime() );
        reminder.setSendDate( reminderDTO.getSendDate() );
        reminder.setSent( reminderDTO.getSent() );
        reminder.setType( reminderDTO.getType() );

        return reminder;
    }

    private UUID reminderFriendId(Reminder reminder) {
        if ( reminder == null ) {
            return null;
        }
        Friend friend = reminder.getFriend();
        if ( friend == null ) {
            return null;
        }
        UUID id = friend.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
