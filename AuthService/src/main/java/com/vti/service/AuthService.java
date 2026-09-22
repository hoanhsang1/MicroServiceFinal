package com.vti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.vti.authen.JwtUtil;
import com.vti.entity.User;
import com.vti.form.AuthRequest;
import com.vti.form.RegisterRequest;
import com.vti.rabbitmqClient.service.RabbitMQSender;
import com.vti.repository.IUserRepository;
import com.vti.entity.enums.UserRole;

@Service
public class AuthService implements IAuthService {
    @Autowired private IUserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private RabbitMQSender rabbitMQSender;

    @Override
    public String login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtUtil.generateToken(user.getUsername(), user.getRole().name(), user.getUserId());
    }

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setUserId(request.getUserId()); // Set user ID from request if provided
        user.setRole(request.getRole() != null ? request.getRole() : UserRole.USER); // Set role from request if provided
        User savedUser = userRepository.save(user);

        rabbitMQSender.sendUserCreatedEvent(savedUser); 

        return savedUser;
    }
}