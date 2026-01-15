package com.citybuilder.model;

/**
 * Mood states for residents.
 */
public enum ResidentMood {
    HAPPY("😊", "Happy"),
    CONTENT("🙂", "Content"),
    NEUTRAL("😐", "Neutral"),
    UNHAPPY("😟", "Unhappy"),
    ANGRY("😡", "Angry");
    
    private final String emoji;
    private final String description;
    
    ResidentMood(String emoji, String description) {
        this.emoji = emoji;
        this.description = description;
    }
    
    public String getEmoji() {
        return emoji;
    }
    
    public String getDescription() {
        return description;
    }
}
