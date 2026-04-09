package com.example.TrackerApp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.TrackerApp.entity.Lift;
import com.example.TrackerApp.repository.LiftRepository;

@RestController
public class LiftRestController
{
    private LiftRepository liftRepository;

    public LiftRestController(LiftRepository liftRepository)
    {
        this.liftRepository = liftRepository;
    }

    @GetMapping("/lifts")
    public List<Lift> getAllLifts()
    {
        return liftRepository.findAll();
    }

    @PostMapping("/lifts")
    public Lift addLift(@RequestBody Lift lift)
    {
        return liftRepository.save(lift);
    }
}