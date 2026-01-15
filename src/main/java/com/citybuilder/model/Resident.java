package com.citybuilder.model;

/**
 * Represents a resident in the city with mood and needs.
 */
public class Resident {
    private String name;
    private ResidentMood mood;
    private int happiness; // 0-100
    private int x, y; // Location
    
    public Resident(int x, int y) {
        this.x = x;
        this.y = y;
        this.happiness = 75;
        this.mood = ResidentMood.CONTENT;
        this.name = "Citizen";
    }
    
    public void updateMood() {
        if (happiness >= 80) {
            mood = ResidentMood.HAPPY;
        } else if (happiness >= 60) {
            mood = ResidentMood.CONTENT;
        } else if (happiness >= 40) {
            mood = ResidentMood.NEUTRAL;
        } else if (happiness >= 20) {
            mood = ResidentMood.UNHAPPY;
        } else {
            mood = ResidentMood.ANGRY;
        }
    }
    
    public void adjustHappiness(int amount) {
        happiness = Math.max(0, Math.min(100, happiness + amount));
        updateMood();
    }
    
    public ResidentMood getMood() {
        return mood;
    }
    
    public int getHappiness() {
        return happiness;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
