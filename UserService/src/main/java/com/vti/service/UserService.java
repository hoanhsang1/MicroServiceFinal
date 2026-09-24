package com.vti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.vti.dto.UserDto;
import com.vti.entity.User;
import com.vti.entity.enums.UserStatus;
import com.vti.form.UserFormUpdate;
import com.vti.repository.IUserRepository;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy user id=" + id));
        return toDto(user);
    }

    @Override
    public List<UserDto> getAllUser() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserDto updateUser(Long id, UserFormUpdate form) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy user id=" + id));
        if (form.getFullName() != null) user.setFullName(form.getFullName());
        if (form.getPhone() != null) user.setPhone(form.getPhone());
        return toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy user id=" + id));
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    @Override
    public UserDto changeUserStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy user id=" + id));
        UserStatus newStatus;
        try {
            newStatus = UserStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status không hợp lệ: " + status);
        }
        user.setStatus(newStatus);
        return toDto(userRepository.save(user));
    }
}