package com.example.TrackerApp.dto;

public class ChartBar
{
    private String dateLabel;
    private int    heightPercent;

    public ChartBar(String dateLabel, int heightPercent)
    {
        this.dateLabel     = dateLabel;
        this.heightPercent = heightPercent;
    }

    public String getDateLabel()     { return dateLabel; }
    public int    getHeightPercent() { return heightPercent; }
}