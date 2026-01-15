package com.citybuilder.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a vehicle (NPC car) in the city.
 */
public class Vehicle {
    private int x, y;
    private Point destination;
    private List<Point> path;
    private VehicleType type;
    private int speed;
    private boolean stopped;
    
    public Vehicle(int x, int y, VehicleType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.speed = type.getSpeed();
        this.path = new ArrayList<>();
        this.stopped = false;
    }
    
    public void move() {
        if (stopped || path.isEmpty()) {
            return;
        }
        
        Point nextPoint = path.get(0);
        
        // Move towards next point
        if (x < nextPoint.x) x++;
        else if (x > nextPoint.x) x--;
        
        if (y < nextPoint.y) y++;
        else if (y > nextPoint.y) y--;
        
        // Check if reached next point
        if (x == nextPoint.x && y == nextPoint.y) {
            path.remove(0);
        }
    }
    
    public void setPath(List<Point> path) {
        this.path = new ArrayList<>(path);
    }
    
    public void stop() {
        this.stopped = true;
    }
    
    public void resume() {
        this.stopped = false;
    }
    
    public boolean isStopped() {
        return stopped;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public VehicleType getType() {
        return type;
    }
}
