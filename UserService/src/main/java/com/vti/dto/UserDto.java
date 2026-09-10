package com.vti.dto;

import java.time.LocalDateTime;

import com.vti.entity.enums.UserStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}