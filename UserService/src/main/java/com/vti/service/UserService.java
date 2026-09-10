package com.vti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vti.entity.User;
import com.vti.form.UserForm;
import com.vti.form.UserFormUpdate;
import com.vti.repository.IUserRepository;
public class UserService implements IUserService {
    @Autowired 
    private IUserRepository userRepository;
    @Override
    public User getUserById(Long id) {
        // TODO Auto-generated method stub
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAllUser() {
        // TODO Auto-generated method stub
        return userRepository.findAll();
    }

    @Override
    public UserForm register(UserForm user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException(
                    "User with username " + user.getUsername() + " already exists."
            );
        } else if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                    "User with email " + user.getEmail() + " already exists."
            );
        } else if (userRepository.existsByPhone(user.getPhone())) {
            throw new IllegalArgumentException(
                    "User with phone " + user.getPhone() + " already exists."
            );
        } else if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException(
                    "Password must be at least 8 characters long."
            );
        } else if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException(
                    "Invalid email format."
            );
        }

        User newUser = new User();

        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setFullName(user.getFullName());
        newUser.setPhone(user.getPhone());

        User savedUser = userRepository.save(newUser);

        return new UserForm(
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getPassword(),
                savedUser.getFullName(),
                savedUser.getPhone()
        );
    }

    public UserFormUpdate updateUser(Long id, UserFormUpdate form) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setFullName(form.getFullName());
        user.setPhone(form.getPhone());

        User updatedUser = userRepository.save(user);
        return new UserFormUpdate(
                updatedUser.getFullName(),
                updatedUser.getPhone()
        );
    }

    @Override
    public void deleteUser(Long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Void changeUserStatus(Long id, String status) {
        // TODO Auto-generated method stub
        return null;
    }

}