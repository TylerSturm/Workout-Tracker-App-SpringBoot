package com.example.TrackerApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User
{
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private double weight;
    private String userName;
    private String password;

    public User()
    {
    }

    public User(String name, double weight, String userName, String password)
    {
        this.name = name;
        this.weight = weight;
        this.userName = userName;
        this.password = password;
    }

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public double getweight()
    {
        return this.weight;
    }

    public String getuserName()
    {
        return this.userName;
    }

    public String getPassword()
    {
        return this.password;
    }
}