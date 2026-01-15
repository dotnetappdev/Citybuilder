package com.citybuilder.model;

/**
 * Represents a resident in the city with mood and needs.
 */
public class Resident {
    private String name;
    private ResidentMood mood;
    private int happiness; // 0-100
    private int x, y; // Location
    private int age; // Age in years
    private int birthYear;
    private boolean sleeping;
    
    public Resident(int x, int y, int birthYear) {
        this.x = x;
        this.y = y;
        this.happiness = 75;
        this.mood = ResidentMood.CONTENT;
        this.name = "Citizen";
        this.age = 0;
        this.birthYear = birthYear;
        this.sleeping = false;
    }
    
    public void updateAge(int currentYear) {
        this.age = currentYear - birthYear;
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
    
    public void updateForTimeOfDay(TimeOfDay timeOfDay) {
        // Residents sleep at night
        sleeping = timeOfDay.isNight();
        
        // Adjust happiness based on time of day
        if (timeOfDay == TimeOfDay.NIGHT && !sleeping) {
            adjustHappiness(-1); // Unhappy if not sleeping at night
        }
    }
    
    public void adjustHappiness(int amount) {
        happiness = Math.max(0, Math.min(100, happiness + amount));
        updateMood();
    }
    
    public boolean isElderly() {
        return age >= 65;
    }
    
    public boolean isAdult() {
        return age >= 18 && age < 65;
    }
    
    public boolean isChild() {
        return age < 18;
    }
    
    public ResidentMood getMood() {
        return mood;
    }
    
    public int getHappiness() {
        return happiness;
    }
    
    public int getAge() {
        return age;
    }
    
    public int getBirthYear() {
        return birthYear;
    }
    
    public boolean isSleeping() {
        return sleeping;
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
