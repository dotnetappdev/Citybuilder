package com.citybuilder.model;

import java.awt.Color;

/**
 * Types of vehicles in the city.
 */
public enum VehicleType {
    CAR(Color.BLUE, 2, "Car"),
    TRUCK(Color.GRAY, 1, "Truck"),
    BUS(Color.ORANGE, 1, "Bus"),
    TAXI(Color.YELLOW, 2, "Taxi"),
    POLICE(Color.BLUE, 3, "Police Car"),
    AMBULANCE(Color.RED, 3, "Ambulance");
    
    private final Color color;
    private final int speed;
    private final String name;
    
    VehicleType(Color color, int speed, String name) {
        this.color = color;
        this.speed = speed;
        this.name = name;
    }
    
    public Color getColor() {
        return color;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public String getName() {
        return name;
    }
}
