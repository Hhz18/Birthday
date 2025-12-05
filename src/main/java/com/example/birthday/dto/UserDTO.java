package com.example.birthday.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    private String id;  // UUID 转为 String
    private String email;
    private String password;
    private String username;
}
