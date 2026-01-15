package com.citybuilder.model;

import java.awt.Point;

/**
 * Camera system that handles view rotation and positioning.
 */
public class Camera {
    private int offsetX;
    private int offsetY;
    private int rotation; // 0, 1, 2, 3 for 0°, 90°, 180°, 270°
    private double zoom;
    
    public Camera() {
        this.offsetX = 400;
        this.offsetY = 100;
        this.rotation = 0;
        this.zoom = 1.0;
    }
    
    public int getOffsetX() {
        return offsetX;
    }
    
    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }
    
    public int getOffsetY() {
        return offsetY;
    }
    
    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }
    
    public void move(int dx, int dy) {
        this.offsetX += dx;
        this.offsetY += dy;
    }
    
    public int getRotation() {
        return rotation;
    }
    
    public void setRotation(int rotation) {
        this.rotation = rotation % 4;
        if (this.rotation < 0) this.rotation += 4;
    }
    
    public void rotateClockwise() {
        rotation = (rotation + 1) % 4;
    }
    
    public void rotateCounterClockwise() {
        rotation = (rotation - 1 + 4) % 4;
    }
    
    public double getZoom() {
        return zoom;
    }
    
    public void setZoom(double zoom) {
        this.zoom = Math.max(0.5, Math.min(2.0, zoom));
    }
    
    /**
     * Apply camera rotation to grid coordinates
     */
    public Point applyRotation(int x, int y, int mapWidth, int mapHeight) {
        int nx = x, ny = y;
        
        switch (rotation) {
            case 1: // 90° clockwise
                nx = y;
                ny = mapWidth - 1 - x;
                break;
            case 2: // 180°
                nx = mapWidth - 1 - x;
                ny = mapHeight - 1 - y;
                break;
            case 3: // 270° clockwise (90° counter-clockwise)
                nx = mapHeight - 1 - y;
                ny = x;
                break;
        }
        
        return new Point(nx, ny);
    }
}
