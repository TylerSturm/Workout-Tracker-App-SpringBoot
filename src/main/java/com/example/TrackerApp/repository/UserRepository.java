package com.example.TrackerApp.repository;

import com.example.TrackerApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    User findByEmail(String email);
    User findByUsername(String username);

    // findByEmailAndPassword has been removed — passwords are hashed so they
    // can never be queried directly. Use findByEmail then BCrypt.matches() instead.
}