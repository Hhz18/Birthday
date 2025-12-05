package com.example.birthday.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FriendDTO {
    private String id;  // UUID 转为 String
    private String userId;  // UUID 转为 String
    private String name;
    private LocalDate birthday;
    private Integer importance;
    private String likes;
    private String note;
    private String gender;
    private Boolean remind;
    private Integer remindDays;

}
