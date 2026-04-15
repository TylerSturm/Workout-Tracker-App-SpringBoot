package com.example.TrackerApp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    // Define repository and password hashing method
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        if (userRepository.findByEmail(user.getEmail()) != null)
        {
            model.addAttribute("message", "Email is already in use.");
            return "signupError";
        }

        if (userRepository.findByUsername(user.getUsername()) != null)
        {
            model.addAttribute("message", "Username is already taken.");
            return "signupError";
        }

        // Hash the plain text password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        model.addAttribute("message", "User created successfully!");
        return "signupSuccess";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model)
    {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute User user, HttpSession session, Model model)
    {
        // Look up by email only 
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword()))
        {
            model.addAttribute("message", "Invalid email or password.");
            return "loginError";
        }

        session.setAttribute("userId",   existingUser.getId());
        session.setAttribute("userName", existingUser.getName());

        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/login";
    }
}