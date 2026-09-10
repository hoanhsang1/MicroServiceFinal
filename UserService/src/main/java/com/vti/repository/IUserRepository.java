package com.vti.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vti.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
