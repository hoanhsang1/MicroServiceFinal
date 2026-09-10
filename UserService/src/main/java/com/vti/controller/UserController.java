package com.vti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vti.dto.UserDto;
import com.vti.form.UserForm;
import com.vti.form.UserFormUpdate;
import com.vti.service.IUserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserForm form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(form));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.login(username, password));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserFormUpdate form) {
        return ResponseEntity.ok(userService.updateUser(id, form));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserDto> changeStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(userService.changeUserStatus(id, status));
    }
}