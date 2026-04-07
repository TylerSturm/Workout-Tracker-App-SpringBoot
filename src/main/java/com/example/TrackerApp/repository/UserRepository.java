package com.example.TrackerApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.TrackerApp.entity.Lift;

public interface UserRepository extends JpaRepository<User, String>
{
    
}