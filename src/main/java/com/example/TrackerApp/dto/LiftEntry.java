package com.example.TrackerApp.dto;

public class LiftEntry
{
    private String liftName;
    private int weight;
    private int reps;
    private int sets;
    private String dateLabel;

    public LiftEntry(String liftName, int weight, int reps, int sets, String dateLabel)
    {
        this.liftName = liftName;
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
        this.dateLabel = dateLabel;
    }

    public String getLiftName() { return liftName; }
    public int getWeight() { return weight; }
    public int getReps() { return reps; }
    public int getSets() { return sets; }
    public String getDateLabel() { return dateLabel; }
}