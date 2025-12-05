package com.example.birthday.repository;

import com.example.birthday.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    //登陆查询
    User findByEmail(String email);
}
