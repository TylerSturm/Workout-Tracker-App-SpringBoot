package com.example.TrackerApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

import com.example.TrackerApp.entity.Lift;

public interface LiftRepository extends JpaRepository<Lift, String>
{
    List<Lift> findByUserIdOrderByIdDesc(long userId);
}