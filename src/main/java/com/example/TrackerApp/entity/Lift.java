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
    private long userId;

    public Lift()
    {

    }

    public Lift(int weight, int reps, String name)
    {
        this.weight = weight;
        this.reps = reps;
        this.name = name;
    }

    public void setId(Long id) { this.id = id; }
    public void setWeight(int weight) { this.weight = weight; }
    public void setReps(int reps) { this.reps = reps; }
    public void setName(String name) {this.name = name; }
    public void setUserId(Long userId) {this.userId = userId; }

    public Long getId() { return this.id; }
    public int getWeight() { return this.weight; }
    public int getReps() { return this.reps; }
    public String getName() { return this.name; }
    public Long getUserId() { return this.userId; }
}