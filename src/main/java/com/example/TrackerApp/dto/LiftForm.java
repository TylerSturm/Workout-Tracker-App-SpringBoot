package com.example.TrackerApp.dto;

public class LiftForm
{
    private String liftName;
    private int weight;
    private int reps;
    private int sets;
    private String notes;

    public LiftForm() {}

    public String getLiftName() { return liftName; }
    public int getWeight() { return weight; }
    public int getReps() { return reps; }
    public int getSets() { return sets == 0 ? 1 : sets; }
    public String getNotes() { return notes; }

    public void setLiftName(String liftName) { this.liftName = liftName; }
    public void setWeight(int weight) { this.weight = weight; }
    public void setReps(int reps) { this.reps = reps; }
    public void setSets(int sets) { this.sets = sets; }
    public void setNotes(String notes) { this.notes = notes; }
}