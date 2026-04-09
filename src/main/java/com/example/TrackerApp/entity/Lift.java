package com.example.TrackerApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Lifts")
public class Lift
{
    @Id
    @GeneratedValue
    private Long id;
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

    public Long getId()
    {
        return this.id;
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