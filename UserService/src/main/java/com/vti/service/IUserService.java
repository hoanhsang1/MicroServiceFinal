package com.vti.service;

import java.util.List;

import com.vti.dto.UserDto;
import com.vti.form.UserFormUpdate;

public interface IUserService {
    UserDto getUserById(Long id);
    List<UserDto> getAllUser();
    UserDto updateUser(Long id, UserFormUpdate form);
    void deleteUser(Long id); // xoá mềm -> set status INACTIVE
    UserDto changeUserStatus(Long id, String status);
}