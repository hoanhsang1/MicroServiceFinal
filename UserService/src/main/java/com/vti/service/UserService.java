package com.vti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.vti.dto.UserDto;
import com.vti.entity.User;
import com.vti.entity.enums.UserStatus;
import com.vti.form.UserForm;
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
    public UserDto register(UserForm form) {
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with username " + form.getUsername() + " already exists.");
        }
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with email " + form.getEmail() + " already exists.");
        }
        if (userRepository.existsByPhone(form.getPhone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with phone " + form.getPhone() + " already exists.");
        }
        if (form.getPassword() == null || form.getPassword().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password must be at least 8 characters long.");
        }
        if (!form.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format.");
        }

        // NOTE (làm sau): password đang lưu plain text, chưa mã hoá — chưa làm security
        User newUser = User.builder()
                .username(form.getUsername())
                .email(form.getEmail())
                .password(form.getPassword())
                .fullName(form.getFullName())
                .phone(form.getPhone())
                .status(UserStatus.ACTIVE)
                .build();

        return toDto(userRepository.save(newUser));
    }

    @Override
    public UserDto login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tài khoản hoặc mật khẩu"));

        // NOTE (làm sau): so sánh password thường, chưa mã hoá — chưa làm security/JWT
        if (!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tài khoản hoặc mật khẩu");
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tài khoản đã bị khoá hoặc vô hiệu hoá");
        }
        return toDto(user);
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
        user.setStatus(UserStatus.INACTIVE); // xoá mềm, không xoá cứng
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