package com.example.TrackerApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.TrackerApp.entity.User;
import com.example.TrackerApp.repository.UserRepository;

@Controller
public class UserController
{

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) 
    {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) 
    {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute User user, Model model) 
    {
        userRepository.save(user);

        model.addAttribute("message", "User created successfully!");
        return "result";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model)
    {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute User user, Model model)
    {
        return "loginSuccess";
    }
}