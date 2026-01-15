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
        
        // Calculate intended movement direction
        int dx = 0, dy = 0;
        if (x < nextPoint.x) dx = 1;
        else if (x > nextPoint.x) dx = -1;
        
        if (y < nextPoint.y) dy = 1;
        else if (y > nextPoint.y) dy = -1;
        
        // Move towards next point
        x += dx;
        y += dy;
        
        // Check if reached next point
        if (x == nextPoint.x && y == nextPoint.y) {
            path.remove(0);
        }
    }
    
    public boolean canMoveToTile(Tile tile) {
        if (tile == null || !tile.isRoad()) {
            return false;
        }
        
        // Calculate direction of movement
        int dx = tile.getX() - x;
        int dy = tile.getY() - y;
        
        // Check if traffic direction allows this movement
        return tile.getTrafficDirection().allowsDirection(dx, dy);
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
