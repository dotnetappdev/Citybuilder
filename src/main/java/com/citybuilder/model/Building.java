package com.citybuilder.model;

/**
 * Represents a building in the city.
 */
public class Building {
    private BuildingType type;
    private String name;
    private int cost;
    private boolean powered;
    private boolean hasWaterAccess;
    
    public Building(BuildingType type) {
        this.type = type;
        this.name = type.getName();
        this.cost = type.getCost();
        this.powered = false;
        this.hasWaterAccess = false;
    }
    
    public BuildingType getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }
    
    public int getCost() {
        return cost;
    }
    
    public boolean isPowered() {
        return powered;
    }
    
    public void setPowered(boolean powered) {
        this.powered = powered;
    }
    
    public boolean hasWaterAccess() {
        return hasWaterAccess;
    }
    
    public void setHasWaterAccess(boolean hasWaterAccess) {
        this.hasWaterAccess = hasWaterAccess;
    }
}
