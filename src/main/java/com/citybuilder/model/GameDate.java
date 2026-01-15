package com.citybuilder.model;

/**
 * Date and time system for the city.
 */
public class GameDate {
    private int year;
    private int month; // 1-12
    private int day; // 1-30 (simplified)
    private int hour; // 0-23
    
    public GameDate(int startYear) {
        this.year = startYear;
        this.month = 1;
        this.day = 1;
        this.hour = 6; // Start at 6 AM
    }
    
    public void advanceHour() {
        hour++;
        if (hour >= 24) {
            hour = 0;
            advanceDay();
        }
    }
    
    public void advanceDay() {
        day++;
        if (day > 30) {
            day = 1;
            advanceMonth();
        }
    }
    
    public void advanceMonth() {
        month++;
        if (month > 12) {
            month = 1;
            year++;
        }
    }
    
    public int getYear() {
        return year;
    }
    
    public int getMonth() {
        return month;
    }
    
    public int getDay() {
        return day;
    }
    
    public int getHour() {
        return hour;
    }
    
    public TimeOfDay getTimeOfDay() {
        return TimeOfDay.fromHour(hour);
    }
    
    public String getFormattedDate() {
        return String.format("%02d/%02d/%04d", month, day, year);
    }
    
    public String getFormattedTime() {
        int displayHour = hour % 12;
        if (displayHour == 0) displayHour = 12;
        String ampm = hour < 12 ? "AM" : "PM";
        return String.format("%d:00 %s", displayHour, ampm);
    }
    
    public int getTotalMonths() {
        return (year - 2000) * 12 + month;
    }
}
