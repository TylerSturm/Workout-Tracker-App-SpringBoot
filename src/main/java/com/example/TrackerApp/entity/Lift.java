package com.example.TrackerApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Lift
{
    @Id
    @GeneratedValue
    private int weight;
    private int reps;
    private String name;

    public Lift()
    {

    }

    public Lift(int weight, int reps, String name)
    {
        this.weight = weight;
        this.reps = reps;
        this.name = name;
    }

    public int getWeight()
    {
        return this.weight;
    }

    public int getReps()
    {
        return this.reps;
    }

    public String getName()
    {
        return this.name;
    }
}