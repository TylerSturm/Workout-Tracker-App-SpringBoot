package com.example.TrackerApp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.TrackerApp.entity.User;
import com.example.TrackerApp.repository.UserRepository;

@RestController
public class UserController
{
    private UserRepository userRepository;

    public UserController(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @GetMapping("/products")
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    @PostMapping("/products")
    public User addUser(@RequestBody User user)
    {
        return userRepository.save(user);
    }
}