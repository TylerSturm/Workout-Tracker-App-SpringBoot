package com.example.TrackerApp.controller;

import jakarta.servlet.http.HttpSession;
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
        User existingUser = userRepository.findByEmailAndPassword(
                user.getEmail(),
                user.getPassword());

        if (existingUser == null)
        {
            model.addAttribute("message", "Invalid email or password.");
            return "loginError";
        }

        // Store the user's ID and name in the session for use across the app
        session.setAttribute("userId", existingUser.getId());
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