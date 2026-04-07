package com.example.TrackerApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User
{
    @Id
    @GeneratedValue
    private String name;
    private double weight;
    private String userName;
    private String password;

    public user(String name, double weight, String userName, String password)
    {
        this.name = name;
        this.weight = weight;
        this.userName = userName;
        this.password = password;
    }

    public int getName()
    {
        return this.name;
    }

    public int getweight()
    {
        return this.weight;
    }

    public String getuserName()
    {
        return this.userName;
    }

    public string getPassword()
    {
        return this.password;
    }
}