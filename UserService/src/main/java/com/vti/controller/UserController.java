package com.vti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import com.vti.dto.UserDto;
import com.vti.form.UserFormUpdate;
import com.vti.service.IUserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody @Valid UserFormUpdate form) {
        return ResponseEntity.ok(userService.updateUser(id, form));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserDto> changeStatus(
        @PathVariable Long id,
        @RequestParam String status,
        @RequestParam(required = false) String currentUserRole) {
        if (!"ADMIN".equals(currentUserRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ ADMIN mới được thay đổi trạng thái người dùng");
        }
        return ResponseEntity.ok(userService.changeUserStatus(id, status));
    }
}