package com.citybuilder.model;

/**
 * Represents a single tile in the city map.
 */
public class Tile {
    private int x;
    private int y;
    private int height; // Terrain height
    private TerrainType terrainType;
    private ZoneType zoneType;
    private Building building;
    private NaturalFeature naturalFeature;
    private boolean hasElectricity;
    private boolean hasWater;
    private TrafficDirection trafficDirection;
    
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.height = 0;
        this.terrainType = TerrainType.GRASS;
        this.zoneType = ZoneType.NONE;
        this.building = null;
        this.naturalFeature = null;
        this.hasElectricity = false;
        this.hasWater = false;
        this.trafficDirection = TrafficDirection.ALL_DIRECTIONS;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public void raiseHeight() {
        if (height < 10) height++;
    }
    
    public void lowerHeight() {
        if (height > -10) height--;
    }
    
    public TerrainType getTerrainType() {
        return terrainType;
    }
    
    public void setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }
    
    public ZoneType getZoneType() {
        return zoneType;
    }
    
    public void setZoneType(ZoneType zoneType) {
        this.zoneType = zoneType;
    }
    
    public Building getBuilding() {
        return building;
    }
    
    public void setBuilding(Building building) {
        this.building = building;
    }
    
    public NaturalFeature getNaturalFeature() {
        return naturalFeature;
    }
    
    public void setNaturalFeature(NaturalFeature naturalFeature) {
        this.naturalFeature = naturalFeature;
    }
    
    public boolean hasElectricity() {
        return hasElectricity;
    }
    
    public void setHasElectricity(boolean hasElectricity) {
        this.hasElectricity = hasElectricity;
    }
    
    public boolean hasWater() {
        return hasWater;
    }
    
    public void setHasWater(boolean hasWater) {
        this.hasWater = hasWater;
    }
    
    public TrafficDirection getTrafficDirection() {
        return trafficDirection;
    }
    
    public void setTrafficDirection(TrafficDirection trafficDirection) {
        this.trafficDirection = trafficDirection;
    }
    
    public void cycleTrafficDirection() {
        this.trafficDirection = trafficDirection.next();
    }
    
    public boolean isRoad() {
        return building != null && 
               (building.getType() == BuildingType.ROAD || 
                building.getType() == BuildingType.ROUNDABOUT);
    }
    
    public boolean isEmpty() {
        return building == null && naturalFeature == null && 
               terrainType != TerrainType.WATER;
    }
    
    public void demolish() {
        this.building = null;
        this.naturalFeature = null;
        this.zoneType = ZoneType.NONE;
        this.trafficDirection = TrafficDirection.ALL_DIRECTIONS;
    }
}
