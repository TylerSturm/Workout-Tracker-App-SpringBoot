package com.example.TrackerApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.TrackerApp.entity.User;

public interface UserRepository extends JpaRepository<User, String>
{
    User findByUsername(String username);
    User findByEmail(String email);
    User findByEmailAndPassword(String username, String password);
}