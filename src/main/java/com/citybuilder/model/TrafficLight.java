package com.citybuilder.model;

/**
 * Represents a traffic light at an intersection.
 */
public class TrafficLight {
    private int x, y;
    private TrafficLightState state;
    private int timer;
    private static final int GREEN_DURATION = 60; // frames
    private static final int YELLOW_DURATION = 20;
    private static final int RED_DURATION = 60;
    
    public TrafficLight(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = TrafficLightState.GREEN;
        this.timer = GREEN_DURATION;
    }
    
    public void update() {
        timer--;
        if (timer <= 0) {
            switchState();
        }
    }
    
    private void switchState() {
        switch (state) {
            case GREEN:
                state = TrafficLightState.YELLOW;
                timer = YELLOW_DURATION;
                break;
            case YELLOW:
                state = TrafficLightState.RED;
                timer = RED_DURATION;
                break;
            case RED:
                state = TrafficLightState.GREEN;
                timer = GREEN_DURATION;
                break;
        }
    }
    
    public boolean shouldStop() {
        return state == TrafficLightState.RED || state == TrafficLightState.YELLOW;
    }
    
    public TrafficLightState getState() {
        return state;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}
