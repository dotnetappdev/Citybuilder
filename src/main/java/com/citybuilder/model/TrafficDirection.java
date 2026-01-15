package com.citybuilder.model;

/**
 * Traffic flow direction for roads.
 */
public enum TrafficDirection {
    NORTH("↑", "North"),
    SOUTH("↓", "South"),
    EAST("→", "East"),
    WEST("←", "West"),
    NORTH_SOUTH("↕", "North-South"),
    EAST_WEST("↔", "East-West"),
    ALL_DIRECTIONS("✚", "All Directions");
    
    private final String arrow;
    private final String description;
    
    TrafficDirection(String arrow, String description) {
        this.arrow = arrow;
        this.description = description;
    }
    
    public String getArrow() {
        return arrow;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean allowsDirection(int dx, int dy) {
        switch (this) {
            case NORTH:
                return dy < 0; // Moving up (negative y)
            case SOUTH:
                return dy > 0; // Moving down (positive y)
            case EAST:
                return dx > 0; // Moving right (positive x)
            case WEST:
                return dx < 0; // Moving left (negative x)
            case NORTH_SOUTH:
                return dy != 0; // Moving vertically
            case EAST_WEST:
                return dx != 0; // Moving horizontally
            case ALL_DIRECTIONS:
                return true; // Any direction
            default:
                return true;
        }
    }
    
    public TrafficDirection next() {
        TrafficDirection[] values = values();
        return values[(ordinal() + 1) % values.length];
    }
}
