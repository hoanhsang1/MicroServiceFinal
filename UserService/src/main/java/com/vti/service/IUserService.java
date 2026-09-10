package com.vti.service;

import java.util.List;

import com.vti.entity.User;
import com.vti.form.UserForm;
import com.vti.form.UserFormUpdate;

public interface IUserService {
    User getUserById(Long id);
    List<User> getAllUser();
    UserForm register(UserForm user);
    UserFormUpdate updateUser(UserFormUpdate userFormUpdate);
    void deleteUser(Long id);
    Void changeUserStatus(Long id, String status);
}
