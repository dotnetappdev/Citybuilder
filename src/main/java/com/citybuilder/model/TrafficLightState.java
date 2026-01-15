package com.citybuilder.model;

import java.awt.Color;

/**
 * States for traffic lights.
 */
public enum TrafficLightState {
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    RED(Color.RED);
    
    private final Color color;
    
    TrafficLightState(Color color) {
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }
}
