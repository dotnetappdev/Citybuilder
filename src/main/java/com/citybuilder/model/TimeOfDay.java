package com.citybuilder.model;

/**
 * Time of day in the city.
 */
public enum TimeOfDay {
    DAWN(0, 6, "Dawn", 0.5f),
    MORNING(6, 12, "Morning", 1.0f),
    AFTERNOON(12, 18, "Afternoon", 1.0f),
    EVENING(18, 21, "Evening", 0.7f),
    NIGHT(21, 24, "Night", 0.3f);
    
    private final int startHour;
    private final int endHour;
    private final String name;
    private final float brightness;
    
    TimeOfDay(int startHour, int endHour, String name, float brightness) {
        this.startHour = startHour;
        this.endHour = endHour;
        this.name = name;
        this.brightness = brightness;
    }
    
    public String getName() {
        return name;
    }
    
    public float getBrightness() {
        return brightness;
    }
    
    public static TimeOfDay fromHour(int hour) {
        hour = hour % 24;
        for (TimeOfDay tod : values()) {
            if (hour >= tod.startHour && hour < tod.endHour) {
                return tod;
            }
        }
        return NIGHT;
    }
    
    public boolean isNight() {
        return this == NIGHT || this == DAWN;
    }
}
